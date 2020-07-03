package com.pritam.listapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pritam.listapp.R
import com.pritam.listapp.databinding.FactItemBinding
import com.pritam.listapp.retrofit.model.Fact

class FactAdapter(val FactList: MutableList<Fact>) : RecyclerView.Adapter<FactAdapter.FactViewHolder>() {

    //this method is returning the view or data binding for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FactViewHolder {
        val binding = DataBindingUtil.inflate<FactItemBinding>(LayoutInflater.from(parent.context), R.layout.fact_item,
            parent, false)
        return FactViewHolder(binding)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: FactViewHolder, position: Int) {
        holder.binding.fact = FactList[position]
        holder.binding.executePendingBindings()
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return FactList.size
    }

    //the class is holding the list view
    class FactViewHolder(val binding: FactItemBinding) : RecyclerView.ViewHolder(binding.root)

}