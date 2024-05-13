package com.example.clusterproject.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.clusterproject.BR
class BaseViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        mViewModel: BaseAndroidViewModel,
        obj: Any?,
        position: Int,
        selectedPosition: Int,
        textVisibility: Boolean
    ) {
        binding.setVariable(BR.mViewModel, mViewModel)
        binding.setVariable(BR.obj, obj)
        binding.setVariable(BR.position, position)
        binding.setVariable(BR.selectedPosition, selectedPosition)
        binding.setVariable(BR.textVisibility, textVisibility)
        binding.executePendingBindings()
    }
}