package com.m3sv.teamtask.presentation.transactions.data

import android.os.Parcel
import android.os.Parcelable
import com.m3sv.common.data.ExchangeRate
import com.m3sv.common.data.ExchangeTransaction
import com.m3sv.teamtask.presentation.currency.CurrencyManager

data class Exchange(
    val transactions: Array<ExchangeTransaction>,
    val rates: Array<ExchangeRate>,
    val transactionsInGold: Map<String, List<CurrencyManager.ExchangeResult>>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createTypedArray(ExchangeTransaction),
        parcel.createTypedArray(ExchangeRate),
        parcel.readInt().let {
            val result = mutableMapOf<String, MutableList<CurrencyManager.ExchangeResult>>()

            for (i in 0..it) {
                result[parcel.readString()] = parcel.createTypedArrayList(CurrencyManager.ExchangeResult)
            }

            result
        }
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Exchange

        if (!transactions.contentEquals(other.transactions)) return false
        if (!rates.contentEquals(other.rates)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = transactions.contentHashCode()
        result = 31 * result + rates.contentHashCode()
        return result
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedArray(transactions, flags)
        parcel.writeTypedArray(rates, flags)
        parcel.writeInt(transactionsInGold.size)
        transactionsInGold.forEach {
            parcel.writeString(it.key)
            parcel.writeTypedList(it.value)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Exchange> {
        override fun createFromParcel(parcel: Parcel): Exchange {
            return Exchange(parcel)
        }

        override fun newArray(size: Int): Array<Exchange?> {
            return arrayOfNulls(size)
        }
    }
}