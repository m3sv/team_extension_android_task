package com.m3sv.network.services

import com.m3sv.common.data.ExchangeRate
import com.m3sv.common.data.ExchangeTransaction
import io.reactivex.Single
import retrofit2.http.GET


interface MerchService {
    @GET("/rates.json")
    fun getRates(): Single<Array<ExchangeRate>>

    @GET("/transactions.json")
    fun getTransactions(): Single<Array<ExchangeTransaction>>
}