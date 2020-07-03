package com.pritam.listapp.utils.databinding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.pritam.listapp.R
import com.squareup.picasso.Picasso

class CustomBindingAdapter {
    companion object {
        // static binding adapter to load url image using picasso
        @BindingAdapter("android:imageHref")
        @JvmStatic
        fun loadImage(factImageView: ImageView, imageHref: String?) {
            if (imageHref != "") {
                Picasso.get()
                    .load(imageHref)
                    .placeholder(R.mipmap.no_image_placeholder)
                    .into(factImageView)
            }
        }
    }
}