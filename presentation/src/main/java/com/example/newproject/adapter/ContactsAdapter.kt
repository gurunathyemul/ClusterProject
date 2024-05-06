package com.example.newproject.adapter

import com.example.domain.model.contacts.Contact
import com.example.newproject.base.BaseAdapter
import com.example.newproject.base.BaseAndroidViewModel

class ContactsAdapter(private val layoutId: Int, baseAndroidViewModel: BaseAndroidViewModel) :
    BaseAdapter(baseAndroidViewModel) {
    private var data: List<Contact?>? = null

    fun setData(data: List<Contact?>?) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getObjForPosition(position: Int) = data?.get(position) ?: 0

    override fun getLayoutIdForPosition(position: Int) = layoutId

    override fun getItemCount(): Int = data?.size ?: 0
}