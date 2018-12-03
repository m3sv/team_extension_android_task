package com.m3sv.common.data

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.IllegalArgumentException
import java.lang.reflect.Type


enum class CurrencyType(val value: String) {
    BRONZE("Bronze"), SILVER("Silver"), GOLD("Gold"), COPPER("Copper");

    companion object {
        fun fromString(currency: String): CurrencyType? {
            return values().firstOrNull { it.value == currency } ?: throw IllegalArgumentException("Unknown currency type: $currency")
        }
    }
}

class CurrencyTypeSerializer : JsonDeserializer<CurrencyType> {
    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): CurrencyType? {
        return CurrencyType.fromString(json.asString)
    }
}

