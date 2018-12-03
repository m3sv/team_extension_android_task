package com.m3sv.teamtask.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.m3sv.common.data.CurrencyType
import com.m3sv.common.data.CurrencyTypeSerializer
import dagger.Module
import dagger.Provides

@Module
object AppModule {
    @Provides
    @JvmStatic
    fun provideGson(): Gson = GsonBuilder()
        .registerTypeAdapter(CurrencyType::class.java, CurrencyTypeSerializer())
        .create()
}
