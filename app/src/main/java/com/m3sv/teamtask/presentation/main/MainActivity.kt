package com.m3sv.teamtask.presentation.main

import android.os.Bundle
import com.m3sv.teamtask.R
import com.m3sv.teamtask.presentation.base.BaseActivity
import com.m3sv.teamtask.presentation.transaction.TransactionFragment
import com.m3sv.teamtask.presentation.transactions.data.Exchange
import com.m3sv.teamtask.presentation.transactions.TransactionsFragment
import java.lang.IllegalArgumentException

class MainActivity : BaseActivity(), TransactionsFragment.OnListFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val transactionsFragment = TransactionsFragment()
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, transactionsFragment).commit()
        }
    }

    override fun onListFragmentInteraction(sku: String, exchange: Exchange) {
        exchange.transactionsInGold[sku]?.let {
            val transactionFragment = TransactionFragment.newInstance(sku, it)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, transactionFragment)
                .addToBackStack(null)
                .commit()
        } ?: IllegalArgumentException("Unknown transaction scu")
    }
}
