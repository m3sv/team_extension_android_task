package com.m3sv.teamtask.presentation.main

import com.google.gson.GsonBuilder
import com.m3sv.common.data.CurrencyType
import com.m3sv.common.data.CurrencyTypeSerializer
import com.m3sv.common.data.ExchangeRate
import com.m3sv.common.data.ExchangeTransaction
import com.m3sv.teamtask.presentation.currency.CurrencyManager
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.*
import org.junit.Test

class CurrencyManagerTest {

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

    @Test
    fun exchangeCurrencyNumberOfChildrenNodes_shouldSucceed() {
        var result = CurrencyManager.exchangeCurrency(CurrencyType.COPPER, 237.0, CurrencyType.SILVER, rates)
        assertEquals(0, result.children.size)

        result = CurrencyManager.exchangeCurrency(CurrencyType.COPPER, 237.0, CurrencyType.COPPER, rates)
        assertEquals(0, result.children.size)

        result = CurrencyManager.exchangeCurrency(CurrencyType.COPPER, 24.42, CurrencyType.GOLD, rates)
        assertThat(36.6871428, equalTo(result.value))
    }

    @Test
    fun calculateTransactionsInGold_shouldSucceed() {
        val sortedTransactions = CurrencyManager.calculateTransactionsSummary(transactions, rates, CurrencyType.GOLD)

        assertEquals(
            77.322564,
            sortedTransactions["T1091"]!!.fold(0.0) { acc, exchangeResult -> acc + exchangeResult.result }, 0.1
        )

        assertEquals(
            19.469099999999997,
            sortedTransactions["I7897"]!!.fold(0.0) { acc, exchangeResult -> acc + exchangeResult.result }, 0.1
        )

        assertEquals(
            34.4,
            sortedTransactions["H2313"]!!.fold(0.0) { acc, exchangeResult -> acc + exchangeResult.result }, 0.1
        )
        assertEquals(
            11.88075,
            sortedTransactions["R1893"]!!.fold(0.0) { acc, exchangeResult -> acc + exchangeResult.result }, 0.1
        )
        assertEquals(
            21.615299999999998,
            sortedTransactions["I1421"]!!.fold(0.0) { acc, exchangeResult -> acc + exchangeResult.result }, 0.1
        )
        assertEquals(
            17.24625,
            sortedTransactions["S9069"]!!.fold(0.0) { acc, exchangeResult -> acc + exchangeResult.result }, 0.1
        )
        assertEquals(
            25.62,
            sortedTransactions["E7084"]!!.fold(0.0) { acc, exchangeResult -> acc + exchangeResult.result }, 0.1
        )

    }

    @Test
    fun silverToGold_shouldSucceed() {
        var exchangedCurrency = CurrencyManager.exchangeCurrency(CurrencyType.SILVER, 15.5, CurrencyType.GOLD, rates)

        assertEquals(
            11.88075,
            exchangedCurrency.value, 0.1
        )

        exchangedCurrency = CurrencyManager.exchangeCurrency(CurrencyType.SILVER, 22.5, CurrencyType.GOLD, rates)

        assertEquals(
            17.246250,
            exchangedCurrency.value, 0.1
        )
    }
}