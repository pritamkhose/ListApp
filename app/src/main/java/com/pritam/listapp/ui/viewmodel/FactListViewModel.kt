package com.pritam.listapp.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.pritam.listapp.database.DatabaseCache
import com.pritam.listapp.repository.FactRepository
import com.pritam.listapp.retrofit.model.Facts

class FactListViewModel(application: Application) : AndroidViewModel(application) {

    // Expose LiveData Facts query so the UI can observe it.
    fun getFactListObservable(context: Context, provideCache: DatabaseCache): LiveData<Facts> {
        //return factListObservable
        return FactRepository.getInstance().getFactList(context, provideCache)
    }
}