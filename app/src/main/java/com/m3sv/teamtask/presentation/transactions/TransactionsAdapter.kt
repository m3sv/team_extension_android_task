package com.m3sv.teamtask.presentation.transactions


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.m3sv.teamtask.R
import com.m3sv.teamtask.presentation.transactions.TransactionsFragment.OnListFragmentInteractionListener
import com.m3sv.teamtask.presentation.transactions.data.Exchange


class TransactionsAdapter(
    var exchange: Exchange,
    private val fragmentListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<TransactionsAdapter.ViewHolder>() {

    private val clickListener: View.OnClickListener

    init {
        clickListener = View.OnClickListener { v ->
            val sku = v.tag as String
            fragmentListener?.onListFragmentInteraction(sku, exchange)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.transaction_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = exchange.transactions[position]

        with(holder) {
            sku.text = item.sku
            currency.text = item.currency.value
            amount.text = item.amount

            with(view) {
                tag = item.sku
                setOnClickListener(clickListener)
            }
        }
    }

    fun setItems(exchange: Exchange) {
        this.exchange = exchange
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = exchange.transactions.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val sku: TextView = view.findViewById(R.id.sku)
        val currency: TextView = view.findViewById(R.id.currency)
        val amount: TextView = view.findViewById(R.id.amount)
    }
}
