package com.example.newproject.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.example.newproject.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

object BindingAdapter {
    private const val TAG = "BindingAdapter"

    @BindingAdapter(value = ["imageUrl", "loader"])
    @JvmStatic
    fun loadImage(view: AppCompatImageView, url: String?, loader: ProgressBar) {
        Log.d(TAG, "loadImageURL: $url")
        loader.visibility = View.VISIBLE
        view.load(url) {
            transformations(RoundedCornersTransformation())
//            error(R.drawable.ic_hindustan) //if url is null or something wrong
//            placeholder(R.drawable.ic_hindustan)//while loading image it will place
            listener(onSuccess = { _, _ ->
                loader.visibility = View.GONE // Hide loader on success
            }, onError = { _, _ ->
                loader.visibility = View.GONE // Hide loader on error
            })
        }
    }

    @BindingAdapter(value = ["photoUri"])
    @JvmStatic
    fun setPhoto(view: AppCompatImageView, photoUri: String?) {
        Log.d(TAG, "photoUri: $photoUri")
        val contentResolver = view.context.contentResolver
        var photoBitmap: Bitmap? = null
        if (photoUri != null) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val inputStream = contentResolver.openInputStream(Uri.parse(photoUri))
                    photoBitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                withContext(Main) {
                    if (photoBitmap != null) {
                        view.load(photoBitmap) {
                            transformations(CircleCropTransformation())
                        }
                    }
                }
            }
        } else {
            view.setImageResource(R.drawable.ic_person)
        }
    }

}