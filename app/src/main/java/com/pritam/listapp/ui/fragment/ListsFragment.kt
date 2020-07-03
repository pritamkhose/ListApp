package com.pritam.listapp.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.pritam.listapp.R
import com.pritam.listapp.databinding.FragmentListsBinding
import com.pritam.listapp.retrofit.model.Fact
import com.pritam.listapp.retrofit.model.Facts
import com.pritam.listapp.retrofit.rest.ApiClient
import com.pritam.listapp.retrofit.rest.ApiInterface
import com.pritam.listapp.ui.adapter.FactAdapter
import com.pritam.listapp.utils.ConnectivityUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListsFragment : Fragment() {

    private val TAG = ListsFragment::class.java.simpleName
    private var inProgress: Boolean = false
    private lateinit var mBinding: FragmentListsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Define the binding
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_lists, container, false)

        val context = activity as Context
        activity?.setTitle(R.string.app_name)

        //Bind the recyclerview and Add a LayoutManager
        mBinding.recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        mBinding.swipeRefreshLayout.setColorSchemeResources(
            R.color.blue,
            R.color.green,
            R.color.orange,
            R.color.red
        )
        mBinding.swipeRefreshLayout.setOnRefreshListener{
            fetchdata(context)
        }
        fetchdata(context)

        return mBinding.getRoot()
    }

    // get data from web service
    private fun fetchdata(context: Context) {
        if (ConnectivityUtils.isNetworkAvailable(context)) {
            if(!inProgress) {
                inProgress = true
                // Show swipe to refresh icon animation
                mBinding.swipeRefreshLayout.isRefreshing = inProgress
                // network is present so will load updated data
                val apiService = ApiClient.client!!.create(ApiInterface::class.java)
                val call = apiService.getFactLists()
                call.enqueue(object : Callback<Facts> {

                    override fun onResponse(call: Call<Facts>, response: Response<Facts>) {
                        inProgress = false
                        // Hide swipe to refresh icon animation
                        mBinding.swipeRefreshLayout.isRefreshing = inProgress
                        val aObj: Facts? = response.body()
                        if (aObj !== null) {
                            Log.d(TAG, aObj.toString())
                            if (aObj.title !== null && aObj.title.length > 0) {
                                activity?.title  = aObj.title
                            }

                            val alList = removeNullItem(aObj.rows)

                            //creating adapter and item adding to adapter of recyclerview
                            if (alList !== null && alList.size > 0) {
                                Log.d(TAG, alList.size.toString())
                                mBinding.recyclerView.adapter = FactAdapter(alList)
                            } else {
                                Snackbar.make(mBinding.root, R.string.noFound, Snackbar.LENGTH_LONG).show()
                            }

                        } else {
                            Snackbar.make(mBinding.root, R.string.noFound, Snackbar.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<Facts>, t: Throwable) {
                        // Log error here since request failed
                        Log.e(TAG, t.toString())
                        inProgress = false
                        mBinding.swipeRefreshLayout.isRefreshing = inProgress
                        Snackbar.make(mBinding.root, R.string.error, Snackbar.LENGTH_LONG).show()
                    }
                })
            }
        } else {
            inProgress = false
            mBinding.swipeRefreshLayout.isRefreshing = inProgress
            // network is not present then show message
            Snackbar.make(mBinding.root, R.string.network_error, Snackbar.LENGTH_LONG)
                .setAction("Retry") {
                    fetchdata(context)
                }.show()
        }
    }

    private fun removeNullItem(rows: MutableList<Fact>): MutableList<Fact> {
        val mutableList = rows.toMutableList()
        for (iCount in rows.indices) {
            // remove item from list when  title, description and imageHrel are null
            if (rows[iCount].imageHref.isNullOrEmpty() && rows[iCount].title.isNullOrEmpty() && rows[iCount].description.isNullOrEmpty()) {
                mutableList.removeAt(iCount)
            }
        }
        return mutableList
    }
}

