package com.m3sv.teamtask.presentation.transactions

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.m3sv.teamtask.R
import com.m3sv.teamtask.presentation.base.BaseFragment
import com.m3sv.teamtask.presentation.transactions.data.Exchange
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber


class TransactionsFragment : BaseFragment() {

    private lateinit var viewModel: TransactionsViewModel

    private lateinit var progress: ProgressBar

    private var listener: OnListFragmentInteractionListener? = null

    private var stateDisposable: Disposable? = null

    private val emptyExchange = Exchange(arrayOf(), arrayOf(), mapOf())

    private val transactionsAdapter: TransactionsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        TransactionsAdapter(emptyExchange, listener)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.setTitle(R.string.transactions)

        val view = inflater.inflate(R.layout.fragment_transactions, container, false)

        with(view.findViewById<RecyclerView>(R.id.transactions)) {
            layoutManager = LinearLayoutManager(context)
            adapter = transactionsAdapter
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        savedInstanceState?.let {
            progress.visibility = if (it.getBoolean(IS_PROGRESS_SHOWING, false)) View.VISIBLE else View.GONE
        }

        transactionsAdapter.setItems(viewModel.exchangeCache ?: emptyExchange)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_PROGRESS_SHOWING, progress.visibility == View.VISIBLE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = getViewModel()

        view.findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener { viewModel.getData() }

        progress = view.findViewById(R.id.progress)

        if (viewModel.isLoading)
            progress.visibility = View.VISIBLE

        stateDisposable = viewModel.state.subscribeBy(onNext = {
            Timber.d("New state: $it")
            when (it) {
                is State.Loading -> progress.visibility = View.VISIBLE
                is State.Error -> {
                    progress.visibility = View.GONE
                    Toast
                        .makeText(context, getString(R.string.loading_error, it.throwable.message), Toast.LENGTH_LONG)
                        .show()
                }
                is State.Success -> {
                    progress.visibility = View.GONE
                    transactionsAdapter.setItems(it.exchange)
                }
                is State.Empty -> progress.visibility = View.GONE
            }
        }, onError = Timber::e)
    }

    override fun onDestroyView() {
        stateDisposable?.takeIf { !it.isDisposed }?.dispose()
        super.onDestroyView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(sku: String, exchange: Exchange)
    }

    companion object {
        private const val IS_PROGRESS_SHOWING = "is_progress_bar_showing"
    }
}
