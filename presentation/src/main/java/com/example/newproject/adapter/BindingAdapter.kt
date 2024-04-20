package com.example.newproject.adapter

import android.util.Log
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.RoundedCornersTransformation

object BindingAdapter {
    private const val TAG = "BindingAdapter"

    @BindingAdapter("imageUrl")
    @JvmStatic
    fun loadImage(view: AppCompatImageView, url: String?) {
        Log.d(TAG, "loadImageURL: $url")
        view.load(url) {
            transformations(RoundedCornersTransformation())
//            error(R.drawable.ic_hindustan) //if url is null or something wrong
//            placeholder(R.drawable.ic_hindustan)//while loading image it will place
        }
    }
}