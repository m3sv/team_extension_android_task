package com.m3sv.teamtask.presentation.transactions

import com.m3sv.common.data.CurrencyType
import com.m3sv.common.data.ExchangeRate
import com.m3sv.common.data.ExchangeTransaction
import com.m3sv.network.ApiManager
import com.m3sv.teamtask.presentation.currency.CurrencyManager
import com.m3sv.teamtask.presentation.transactions.data.Exchange
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class TransactionsRepository @Inject constructor(private val apiManager: ApiManager) {

    fun getData(): Observable<Exchange> {
        return Single
            .zip(
                apiManager.getTransactions(),
                apiManager.getRates(),
                BiFunction { transactions: Array<ExchangeTransaction>, rates: Array<ExchangeRate> ->
                    val sortedTransactions =
                        CurrencyManager.calculateTransactionsSummary(transactions, rates, CurrencyType.GOLD)

                    Exchange(transactions, rates, sortedTransactions)
                })
            .flatMapObservable { Observable.just(it) }
            .subscribeOn(Schedulers.io())
    }
}
