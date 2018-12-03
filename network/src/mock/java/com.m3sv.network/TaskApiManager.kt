package com.m3sv.network

import com.google.gson.GsonBuilder
import com.m3sv.common.data.CurrencyType
import com.m3sv.common.data.CurrencyTypeSerializer
import com.m3sv.common.data.ExchangeRate
import com.m3sv.common.data.ExchangeTransaction
import com.m3sv.network.services.MerchService
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TaskApiManager @Inject constructor(private val merchService: MerchService) : ApiManager {

    private val mockRates =
        """[{ "from": "Bronze", "to": "Silver", "rate": "1.37" }, { "from": "Silver", "to": "Bronze", "rate": "0.73" },
{ "from": "Bronze", "to": "Gold", "rate": "1.05" },
{ "from": "Gold", "to": "Bronze", "rate": "0.95" }, { "from": "Silver", "to": "Copper", "rate": "0.51" }, {
"from": "Copper", "to": "Silver", "rate": "1.96" }]
    """.trimIndent()

    private val mockTransactions =
        """[{"sku":"T1091","amount":"24.6","currency":"Copper"},{"sku":"I7897","amount":"25.4","currency":"Silver"},{"sku":"H2313","amount":"34.4","currency":"Gold"},
{"sku":"R1893","amount":"15.5","currency":"Silver"},{"sku":"T1091","amount":"22.2","currency":"Gold"},{"sku":"I1421","amount":"28.2","currency":"Silver"},
{"sku":"S9069","amount":"22.5","currency":"Silver"},{"sku":"E7084","amount":"24.4","currency":"Bronze"},{"sku":"T1091","amount":"17.3","currency":"Bronze"}]
    """.trimIndent()

    private val gson = GsonBuilder()
        .registerTypeAdapter(CurrencyType::class.java, CurrencyTypeSerializer())
        .create()

    private val rates = gson.fromJson(mockRates, Array<ExchangeRate>::class.java)

    private val transactions = gson.fromJson(mockTransactions, Array<ExchangeTransaction>::class.java)
    override fun getRates(): Single<Array<ExchangeRate>> =
        Single.just(rates)
            .zipWith(Single.timer(5000, TimeUnit.MILLISECONDS), BiFunction { rates, _ -> rates })

    override fun getTransactions(): Single<Array<ExchangeTransaction>> = Single.just(transactions)
        .zipWith(Single.timer(5000, TimeUnit.MILLISECONDS), BiFunction { transactions, _ -> transactions })
}