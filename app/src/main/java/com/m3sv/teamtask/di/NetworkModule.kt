package com.m3sv.teamtask.di

import com.google.gson.Gson
import com.m3sv.network.ApiManager
import com.m3sv.network.BuildConfig
import com.m3sv.network.TaskApiManager
import com.m3sv.network.services.MerchService
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


@Module
interface NetworkBindingModule {
    @Binds
    fun bindApiManager(apiManager: TaskApiManager): ApiManager
}

@Module
object NetworkModule {
    @Provides
    @JvmStatic
    fun provideMerchService(retrofit: Retrofit): MerchService =
        retrofit.create(MerchService::class.java)

    @Provides
    @JvmStatic
    fun provideRetrofit(gson: Gson): Retrofit =
        Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
}