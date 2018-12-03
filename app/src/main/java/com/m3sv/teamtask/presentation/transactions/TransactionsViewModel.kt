package com.m3sv.teamtask.presentation.transactions

import com.m3sv.teamtask.presentation.base.BaseViewModel
import com.m3sv.teamtask.presentation.transactions.data.Exchange
import hu.akarnokd.rxjava2.subjects.DispatchWorkSubject
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class TransactionsViewModel @Inject constructor(private val repository: TransactionsRepository) : BaseViewModel() {

    private val stateSubject = DispatchWorkSubject.create<State>(Schedulers.trampoline())

    val state: Observable<State> = stateSubject.observeOn(AndroidSchedulers.mainThread())

    var exchangeCache: Exchange? = null
        private set

    var isLoading: Boolean = false
        private set

    fun getData() {
        repository
            .getData()
            .doOnSubscribe { setState(State.Loading) }
            .subscribeBy(onNext = {
                exchangeCache = it
                setState(State.Success(it))
            }, onError = {
                Timber.e(it, "Error has occurred while updating data")
                setState(State.Error(it))
            })
    }

    private fun setState(state: State) {
        isLoading = state == State.Loading

        stateSubject.onNext(state)
    }
}


sealed class State {
    object Loading : State()
    object Empty : State()
    class Success(val exchange: Exchange) : State()
    class Error(val throwable: Throwable) : State()
}