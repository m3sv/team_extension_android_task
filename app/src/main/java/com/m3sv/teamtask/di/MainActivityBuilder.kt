package com.m3sv.teamtask.di

import androidx.lifecycle.ViewModel
import com.m3sv.teamtask.presentation.main.MainActivity
import com.m3sv.teamtask.presentation.transaction.TransactionFragment
import com.m3sv.teamtask.presentation.transactions.TransactionsViewModel
import com.m3sv.teamtask.presentation.transactions.TransactionsFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap


@Module
interface MainActivityBuilder {
    @ContributesAndroidInjector
    fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    fun contributeTransactionsFragment(): TransactionsFragment

    @ContributesAndroidInjector
    fun contributeTransactionFragment(): TransactionFragment

    @Binds
    @IntoMap
    @ViewModelKey(TransactionsViewModel::class)
    fun bindTransactionsViewModel(transactionsViewModel: TransactionsViewModel): ViewModel
}