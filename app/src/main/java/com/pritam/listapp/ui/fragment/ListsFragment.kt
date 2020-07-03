package com.pritam.listapp.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.pritam.listapp.retrofit.rest.ApiInterface
import com.pritam.listapp.R
import com.pritam.listapp.retrofit.model.Fact
import com.pritam.listapp.retrofit.model.Facts
import com.pritam.listapp.retrofit.rest.ApiClient
import com.pritam.listapp.ui.adapter.FactAdapter
import com.pritam.listapp.utils.ConnectivityUtils

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListsFragment : Fragment() {

    private val TAG = ListsFragment::class.java.simpleName
    private lateinit var rootView: View
    private var inProgress: Boolean = false
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_lists, container, false)

        val context = activity as Context
        activity?.setTitle(R.string.app_name);

        //Bind the recyclerview
        recyclerView = rootView.findViewById(R.id.recyclerView) as RecyclerView

        //Add a LayoutManager
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)

        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_layout) as SwipeRefreshLayout
        swipeRefreshLayout.setColorSchemeResources(
            R.color.blue,
            R.color.green,
            R.color.orange,
            R.color.red
        )
        swipeRefreshLayout.setOnRefreshListener{
            fetchdata(context)
        }
        fetchdata(context)

        return rootView
    }

    private fun fetchdata(context: Context) {
        if (ConnectivityUtils.isNetworkAvailable(context)) {
            if(!inProgress) {
                inProgress = true
                // Show swipe to refresh icon animation
                swipeRefreshLayout.isRefreshing = inProgress
                // network is present so will load updated data
                val apiService = ApiClient.client!!.create(ApiInterface::class.java)
                val call = apiService.getFactLists()
                call.enqueue(object : Callback<Facts> {

                    override fun onResponse(call: Call<Facts>, response: Response<Facts>) {
                        inProgress = false
                        // Hide swipe to refresh icon animation
                        swipeRefreshLayout.isRefreshing = inProgress
                        val aObj: Facts? = response.body()
                        if (aObj !== null) {
                            Log.d(TAG, aObj.toString())
                            if (aObj.title !== null && aObj.title.length > 0) {
                                activity?.title  = aObj.title
                            }

                            val alList = removeNullItem(aObj.rows);

                            //creating adapter and item adding to adapter of recyclerview
                            if (alList !== null && alList.size > 0) {
                                Log.d(TAG, alList.size.toString())
                                recyclerView.adapter = FactAdapter(alList);
                            } else {
                                Snackbar.make(rootView, R.string.noFound, Snackbar.LENGTH_LONG).show();
                            }

                        } else {
                            Snackbar.make(rootView, R.string.noFound, Snackbar.LENGTH_LONG).show();
                        }
                    }

                    override fun onFailure(call: Call<Facts>, t: Throwable) {
                        // Log error here since request failed
                        Log.e(TAG, t.toString())
                        inProgress = false
                        swipeRefreshLayout.isRefreshing = inProgress
                        Snackbar.make(rootView, R.string.error, Snackbar.LENGTH_LONG).show();
                    }
                })
            }
        } else {
            // network is not present then show message
            Snackbar.make(rootView, R.string.network_error, Snackbar.LENGTH_LONG)
                .setAction("Retry") {
                    fetchdata(context)
                }.show();
        }
    }

    private fun removeNullItem(rows: MutableList<Fact>): MutableList<Fact> {
        val mutableList = rows.toMutableList()
        for (iCount in rows.indices) {
            // remove item from list when  title, description and imageHrel are null
            if (rows[iCount].imageHref.isNullOrEmpty() && rows[iCount].title.isNullOrEmpty() && rows[iCount].description.isNullOrEmpty()) {
                mutableList.removeAt(iCount)
                Log.d(TAG + "remove", rows[iCount].imageHref.toString())
            } else {
                Log.d(TAG, rows[iCount].toString())
            }
        }
        return mutableList
    }
}

