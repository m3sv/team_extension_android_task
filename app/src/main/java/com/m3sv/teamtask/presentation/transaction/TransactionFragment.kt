package com.m3sv.teamtask.presentation.transaction

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.m3sv.common.data.CurrencyType
import com.m3sv.teamtask.R
import com.m3sv.teamtask.presentation.base.BaseFragment
import com.m3sv.teamtask.presentation.currency.CurrencyManager
import com.m3sv.teamtask.presentation.transactions.data.Exchange
import java.util.ArrayList

class TransactionFragment : BaseFragment() {

    private val transactionAdapter: TransactionAdapter = TransactionAdapter(mutableListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            activity?.title = it.getString(SKU, "Unknown transaction")
            val exchange = it.getParcelableArrayList<CurrencyManager.ExchangeResult>(EXCHANGE)
            transactionAdapter.setItems(exchange ?: listOf())
        }

        val view = inflater.inflate(R.layout.fragment_transaction, container, false)

        with(view as RecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = transactionAdapter
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        savedInstanceState?.let {
            transactionAdapter.setItems(it.getParcelableArrayList(TRANSACTIONS) ?: mutableListOf())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(TRANSACTIONS, transactionAdapter.items as ArrayList<out Parcelable>)
    }

    companion object {
        private const val TRANSACTIONS = "transactions"
        private const val SKU = "sku"
        private const val EXCHANGE = "exchange"

        fun newInstance(sku: String, exchange: List<CurrencyManager.ExchangeResult>): TransactionFragment {
            return TransactionFragment().also { fragment ->
                fragment.arguments = Bundle()
                    .also { bundle ->
                        bundle.putString(SKU, sku)
                        bundle.putParcelableArrayList(EXCHANGE, exchange as ArrayList<out Parcelable>)
                    }
            }
        }
    }
}
