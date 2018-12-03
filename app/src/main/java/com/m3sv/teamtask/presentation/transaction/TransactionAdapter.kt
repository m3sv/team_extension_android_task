package com.m3sv.teamtask.presentation.transaction


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.m3sv.teamtask.R
import com.m3sv.teamtask.presentation.currency.CurrencyManager


class TransactionAdapter(val items: MutableList<CurrencyManager.ExchangeResult>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HOLDER_FOOTER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.transaction_detail_item, parent, false)
                FooterViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.transaction_detail_item, parent, false)
                ItemViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FooterViewHolder -> {
                with(holder) {
                    sku.text = holder.view.context.getString(
                        R.string.sum_in_gold,
                        items.fold(0.0) { acc, exchangeResult -> acc + exchangeResult.result })
                }
            }
            is ItemViewHolder -> {
                val item = items[position]

                with(holder) {
                    sku.text = item.exchangeTransaction.sku
                    currency.text = item.exchangeTransaction.currency.value
                    amount.text = item.exchangeTransaction.amount
                    amountInGold.text = holder.view.context.getString(R.string.transaction_in_gold, item.result)
                }
            }
        }

    }

    fun setItems(items: List<CurrencyManager.ExchangeResult>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        if (isFooter(position))
            return HOLDER_FOOTER

        return HOLDER_ITEM
    }

    private fun isFooter(position: Int) = position == items.size

    override fun getItemCount(): Int = items.size + 1

    inner class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val sku: TextView = view.findViewById(R.id.sku)
        val currency: TextView = view.findViewById(R.id.currency)
        val amount: TextView = view.findViewById(R.id.amount)
        val amountInGold: TextView = view.findViewById(R.id.amount_in_gold)
    }

    inner class FooterViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val sku: TextView = view.findViewById(R.id.sku)
        val currency: TextView = view.findViewById(R.id.currency)
        val amount: TextView = view.findViewById(R.id.amount)
        val amountInGold: TextView = view.findViewById(R.id.amount_in_gold)
    }

    companion object {
        private const val HOLDER_ITEM = 0
        private const val HOLDER_FOOTER = 1
    }
}
