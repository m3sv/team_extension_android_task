package com.m3sv.network

import com.m3sv.common.data.ExchangeRate
import com.m3sv.common.data.ExchangeTransaction
import io.reactivex.Single


interface ApiManager {
    fun getRates(): Single<Array<ExchangeRate>>

    fun getTransactions(): Single<Array<ExchangeTransaction>>
}