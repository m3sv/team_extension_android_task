package com.m3sv.common.data

import android.os.Parcel
import android.os.Parcelable


data class ExchangeRate(
    val from: CurrencyType,
    val to: CurrencyType,
    val rate: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        CurrencyType.values()[parcel.readInt()],
        CurrencyType.values()[parcel.readInt()],
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(from.ordinal)
        parcel.writeInt(to.ordinal)
        parcel.writeString(rate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ExchangeRate> {
        override fun createFromParcel(parcel: Parcel): ExchangeRate {
            return ExchangeRate(parcel)
        }

        override fun newArray(size: Int): Array<ExchangeRate?> {
            return arrayOfNulls(size)
        }
    }
}