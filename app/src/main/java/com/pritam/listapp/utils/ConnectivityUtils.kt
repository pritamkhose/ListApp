package com.pritam.listapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectivityUtils {
    companion object {

        // Return current network connection status
        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
            return if (connectivityManager is ConnectivityManager) {
                val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
                networkInfo?.isConnected ?: false
            } else false
        }
    }
}