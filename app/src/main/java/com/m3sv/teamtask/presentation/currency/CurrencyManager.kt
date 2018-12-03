package com.m3sv.teamtask.presentation.currency

import android.os.Parcel
import android.os.Parcelable
import com.m3sv.common.data.CurrencyType
import com.m3sv.common.data.ExchangeRate
import com.m3sv.common.data.ExchangeTransaction
import timber.log.Timber


object CurrencyManager {
    fun exchangeCurrency(
        origin: CurrencyType,
        value: Double,
        target: CurrencyType,
        rates: Array<ExchangeRate>
    ): RateNode {
        val currencyTree =
            RateNode(mutableListOf(), origin, value = value)

        if (origin == target)
            return currencyTree

        rates.firstOrNull { it.from == origin && it.to == target }?.let { exchange ->
            return currencyTree.also { currencyTree.value *= exchange.rate.toDouble() }
        }

        fun buildTree(parentNode: RateNode) {
            Timber.d("Processing for node: $parentNode")

            rates.forEach {
                if (it.from == parentNode.currency && it.to == target) {
                    val newNode = RateNode(
                        mutableListOf(),
                        it.to,
                        parentNode,
                        it
                    )
                    parentNode.children.add(newNode)

                    // defensive copy in case of multiple paths to acquire currency
                    var finalValue = currencyTree.value

                    var parent = newNode.parent
                    while (parent != null) {
                        parent.exchangeRate?.rate?.toDouble()?.let { exchangeRate ->
                            finalValue *= exchangeRate
                        }

                        parent = parent.parent
                    }

                    finalValue *= it.rate.toDouble()
                    // the last one, while not always the most efficient one, will be written as a final value,
                    // protects against multiple nodes modifying the final result
                    currencyTree.value = finalValue
                    return@forEach
                }

                if (it.to == parentNode.currency) {
                    return@forEach
                }

                var parent = parentNode.parent
                while (parent != null) {
                    if (parent.currency == it.to) {
                        return@forEach
                    }

                    parent = parent.parent
                }

                if (it.from == parentNode.currency) {
                    val newNode = RateNode(
                        mutableListOf(),
                        it.to,
                        parentNode,
                        it
                    )
                    parentNode.children.add(newNode)
                    buildTree(newNode)
                }
            }
        }

        buildTree(currencyTree)

        // for debug purposes
        fun printChildren(node: RateNode) {
            if (node.children.isEmpty())
                return

            node.children.forEach {
                Timber.d("child: $it")
                printChildren(it)
            }
        }

        printChildren(currencyTree)

        return currencyTree
    }

    private fun calculateTransactionSummary(
        transaction: ExchangeTransaction,
        rates: Array<ExchangeRate>,
        targetCurrency: CurrencyType
    ) = ExchangeResult(
        transaction,
        exchangeCurrency(
            transaction.currency,
            transaction.amount.toDouble(),
            targetCurrency,
            rates
        ).value
    )

    fun calculateTransactionsSummary(
        transactions: Array<ExchangeTransaction>,
        rates: Array<ExchangeRate>,
        targetCurrency: CurrencyType
    ): Map<String, List<ExchangeResult>> =
        transactions.asSequence()
            .fold(mutableMapOf<String, MutableList<ExchangeResult>>()) { acc, transaction ->
                acc[transaction.sku]?.add(
                    calculateTransactionSummary(
                        transaction,
                        rates,
                        targetCurrency
                    )
                ) ?: run {
                    acc[transaction.sku] = mutableListOf(
                        calculateTransactionSummary(
                            transaction,
                            rates,
                            targetCurrency
                        )
                    )
                }

                acc
            }

    data class RateNode(
        val children: MutableList<RateNode>,
        val currency: CurrencyType,
        val parent: RateNode? = null,
        val exchangeRate: ExchangeRate? = null,
        var value: Double = 0.0
    ) {
        override fun toString(): String = "$currency, parent: [$parent], value: $value, exchangeRate: $exchangeRate"
    }

    data class ExchangeResult(val exchangeTransaction: ExchangeTransaction, val result: Double) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readParcelable(ExchangeTransaction::class.java.classLoader),
            parcel.readDouble()
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeParcelable(exchangeTransaction, flags)
            parcel.writeDouble(result)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<ExchangeResult> {
            override fun createFromParcel(parcel: Parcel): ExchangeResult {
                return ExchangeResult(parcel)
            }

            override fun newArray(size: Int): Array<ExchangeResult?> {
                return arrayOfNulls(size)
            }
        }
    }
}