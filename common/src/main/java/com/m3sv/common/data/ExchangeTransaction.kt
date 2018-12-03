package com.m3sv.common.data

import android.os.Parcel
import android.os.Parcelable


data class ExchangeTransaction(val sku: String, val amount: String, val currency: CurrencyType) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        CurrencyType.values()[parcel.readInt()]
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(sku)
        parcel.writeString(amount)
        parcel.writeInt(currency.ordinal)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ExchangeTransaction> {
        override fun createFromParcel(parcel: Parcel): ExchangeTransaction {
            return ExchangeTransaction(parcel)
        }

        override fun newArray(size: Int): Array<ExchangeTransaction?> {
            return arrayOfNulls(size)
        }
    }
}