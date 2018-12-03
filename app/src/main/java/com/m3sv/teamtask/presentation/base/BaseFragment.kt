package com.m3sv.teamtask.presentation.base

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.m3sv.teamtask.di.ViewModelFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject


abstract class BaseFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    protected inline fun <reified T : ViewModel> getViewModel(): T {
        return ViewModelProviders.of(activity as FragmentActivity, viewModelFactory).get(T::class.java)
    }
}