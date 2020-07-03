package com.pritam.listapp.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pritam.listapp.R
import com.pritam.listapp.retrofit.model.Fact
import com.squareup.picasso.Picasso

class FactAdapter(val FactList: MutableList<Fact>) : RecyclerView.Adapter<FactAdapter.FactViewHolder>() {

    lateinit var mContext: Context

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FactViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.fact_item, parent, false)
        mContext = parent.context
        return FactViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: FactViewHolder, position: Int) {
        holder.bindItems(FactList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return FactList.size
    }

    //the class is holding the list view
    class FactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(item: Fact) {
            val tvname = itemView.findViewById(R.id.tv_title) as TextView
            tvname.text = item.title
            val tvdesp = itemView.findViewById(R.id.tv_description) as TextView
            if(item.description !== null){
                tvdesp.text = item.description
            }
            val img_fact = itemView.findViewById(R.id.img_fact) as ImageView
            if (item.imageHref !== null && item.imageHref!!.length > 5) {
                Picasso.get()
                    .load(item.imageHref)
                    .placeholder(R.mipmap.no_image_placeholder)
                    .into(img_fact)
            } else {
                img_fact.visibility = View.GONE
            }
        }
    }

}