package com.pritam.listapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.pritam.listapp.R
import com.pritam.listapp.databinding.FragmentListsBinding
import com.pritam.listapp.helpers.Injector
import com.pritam.listapp.retrofit.model.Fact
import com.pritam.listapp.retrofit.model.Facts
import com.pritam.listapp.ui.adapter.FactAdapter
import com.pritam.listapp.ui.viewmodel.FactListViewModel

class ListsFragment : Fragment() {

    private lateinit var mBinding: FragmentListsBinding
    private var factAdapter: FactAdapter? = null
    private var factList = mutableListOf<Fact>()
    private lateinit var viewModel: FactListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Define the binding
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_lists, container, false)

        // Recycler Adapter
        factAdapter = FactAdapter(factList)
        mBinding.recyclerView.adapter = factAdapter

        //Bind the recyclerview and Add a LayoutManager
        mBinding.recyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        mBinding.swipeRefreshLayout.setColorSchemeResources(
            R.color.blue,
            R.color.green,
            R.color.orange,
            R.color.red
        )
        mBinding.swipeRefreshLayout.setOnRefreshListener {
            observeViewModel()
        }

        viewModel = ViewModelProvider(this).get(FactListViewModel::class.java)
        observeViewModel()

        return mBinding.root
    }

    // get data from web service and update UI
    private fun observeViewModel() {
        // Shows swipe to refresh icon animation
        mBinding.swipeRefreshLayout.isRefreshing = true
        // Update the list when the data changes
        viewModel.getFactListObservable(requireActivity(), Injector.provideCache(requireActivity()))
            .observe(viewLifecycleOwner, { fact ->
                if (fact != null) {
                    // Hide swipe to refresh icon animation
                    mBinding.swipeRefreshLayout.isRefreshing = false
                    //get title & rows from factResponse
                    val errorText = fact.error
                    if (errorText == resources.getString(R.string.noFound) || errorText == resources.getString(
                            R.string.error
                        )
                    ) {
                        errorMsg(errorText)
                    } else if (errorText == resources.getString(R.string.network_error)) {
                        if (fact.title.isNullOrEmpty()) {
                            errorMsg("${resources.getString(R.string.noFound)}, $errorText")
                        } else {
                            errorMsg("${resources.getString(R.string.cache_data)}, $errorText")
                            updateUIData(fact)
                        }
                    } else {
                        // show success message - Data fetch successfully!
                        Snackbar.make(
                            mBinding.root,
                            resources.getString(R.string.success),
                            Snackbar.LENGTH_SHORT
                        ).show()
                        updateUIData(fact)
                    }
                }
            })
    }

    private fun updateUIData(fact: Facts) {
        val title = fact.title
        activity?.title = title
        if (fact.rows != null) {
            val mutableList = removeNullItem(fact.rows!!)
            mBinding.recyclerView.setItemViewCacheSize(mutableList.size)
            // clear list, add new items in list and refresh it using notifyDataSetChanged
            factList.clear()
            factList.addAll(mutableList)
            factAdapter?.notifyDataSetChanged()
        }
    }

    private fun errorMsg(title: String) {
        // show error message
        Snackbar.make(mBinding.root, title, Snackbar.LENGTH_LONG)
            .setAction("Retry") {
                observeViewModel()
            }.show()
    }

    fun removeNullItem(rows: MutableList<Fact>): MutableList<Fact> {
        val mutableList = rows.toMutableList()
        for (iCount in rows.indices) {
            // remove item from list when  title, description and imageHref are null
            if (rows[iCount].imageHref.isNullOrEmpty()
                && rows[iCount].title.isNullOrEmpty()
                && rows[iCount].description.isNullOrEmpty()
            ) {
                mutableList.removeAt(iCount)
            }
        }
        return mutableList
    }
}

