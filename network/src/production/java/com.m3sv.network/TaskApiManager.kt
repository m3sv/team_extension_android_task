package com.m3sv.network

import com.m3sv.common.data.ExchangeRate
import com.m3sv.common.data.ExchangeTransaction
import com.m3sv.network.services.MerchService
import io.reactivex.Single
import javax.inject.Inject

class TaskApiManager @Inject constructor(private val merchService: MerchService) : ApiManager {
    override fun getRates(): Single<Array<ExchangeRate>> = merchService.getRates()

    override fun getTransactions(): Single<Array<ExchangeTransaction>> = merchService.getTransactions()
}