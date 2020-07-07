package com.pritam.listapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    ): View? {
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
            observeViewModel(viewModel)
        }

        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FactListViewModel::class.java)
        observeViewModel(viewModel)
    }

    // get data from web service and update UI
    private fun observeViewModel(viewModel: FactListViewModel) {
        // Shows swipe to refresh icon animation
        mBinding.swipeRefreshLayout.isRefreshing = true
        // Update the list when the data changes
        viewModel.getFactListObservable(activity!!, Injector.provideCache(activity!!))
            .observe(viewLifecycleOwner, Observer<Facts> { fact ->
                // Hide swipe to refresh icon animation
                mBinding.swipeRefreshLayout.isRefreshing = false
                if (fact != null) {
                    //get title & rows from factResponse
                    val title = fact.title
                    activity?.title = title
                    val mutableList = removeNullItem(fact.rows)
                    mBinding.recyclerView.setItemViewCacheSize(mutableList.size)
                    // clear list, add new items in list and refresh it using notifyDataSetChanged
                    factList.clear()
                    factList.addAll(mutableList)
                    factAdapter?.notifyDataSetChanged()
                }
            })
    }

    fun removeNullItem(rows: MutableList<Fact>): MutableList<Fact> {
        val mutableList = rows.toMutableList()
        for (iCount in rows.indices) {
            // remove item from list when  title, description and imageHref are null
            if (rows[iCount].imageHref.isNullOrEmpty() && rows[iCount].title.isNullOrEmpty() && rows[iCount].description.isNullOrEmpty()) {
                mutableList.removeAt(iCount)
            }
        }
        return mutableList
    }
}

