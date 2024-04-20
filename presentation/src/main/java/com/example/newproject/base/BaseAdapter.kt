package com.example.newproject.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.newproject.util.Commons.textVisibility

abstract class BaseAdapter(private var mViewModel: BaseAndroidViewModel) :
    RecyclerView.Adapter<BaseViewHolder?>() {
    var selectedPosition = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val obj = getObjForPosition(position)
        holder.bind(mViewModel, obj, position, selectedPosition, textVisibility)
    }

    override fun getItemViewType(position: Int): Int =getLayoutIdForPosition(position)
    protected abstract fun getObjForPosition(position: Int): Any
    protected abstract fun getLayoutIdForPosition(position: Int): Int
}