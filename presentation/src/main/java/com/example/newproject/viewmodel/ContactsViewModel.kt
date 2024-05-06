package com.example.newproject.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.domain.base.UseCaseCallback
import com.example.domain.exceptions.ErrorModel
import com.example.domain.model.contacts.Contact
import com.example.domain.usecase.contacts.DeleteContactsUseCase
import com.example.domain.usecase.contacts.GetContactsUseCase
import com.example.newproject.R
import com.example.newproject.adapter.ContactsAdapter
import com.example.newproject.base.BaseAndroidViewModel
import com.example.newproject.other.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//
@HiltViewModel
class ContactsViewModel @Inject constructor(
    context: Application,
    private val getContactsUseCase: GetContactsUseCase,
    private val deleteContactsUseCase: DeleteContactsUseCase,
) :
    BaseAndroidViewModel(context) {

    private val _contactsLIst: MutableStateFlow<Resource<List<Contact>>> =
        MutableStateFlow(Resource.Loading())

    // Expose the StateFlow to observe changes
    val contactsList: StateFlow<Resource<List<Contact>>>
        get() = _contactsLIst

    val contactIdList: MutableList<String> = mutableListOf()

    var contactsAdapter: ContactsAdapter? = null
        private set

    init {
        contactsAdapter = ContactsAdapter(R.layout.rv_item_contact, this)
    }

    fun getContactsList(name: String? = null) {
        _contactsLIst.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            getContactsUseCase.call(name, object : UseCaseCallback<List<Contact>> {
                override fun onSuccess(result: List<Contact>) {
                    Log.d(TAG, "onSuccess:: $result")
                    _contactsLIst.value = Resource.Success(result)
                }

                override fun onError(errorModel: ErrorModel?) {
                    Log.d(TAG, "onError:: ${errorModel?.message}(${errorModel?.errorStatus})")
                    _contactsLIst.value = Resource.Error(errorModel?.message!!)
                }
            })
        }
    }

    fun setContactData(contactList: List<Contact?>?) {
        contactsAdapter?.setData(contactList)
    }

    fun deleteContacts() {
        viewModelScope.launch {
            deleteContactsUseCase.call(contactIdList.toTypedArray(),
                object : UseCaseCallback<String> {
                    override fun onSuccess(result: String) {
                        Log.d(TAG, "onSuccess:: $result")
                        contactIdList.clear()
                        getContactsList()
                    }

                    override fun onError(errorModel: ErrorModel?) {
                        Log.d(TAG, "onError:: ${errorModel?.message}(${errorModel?.errorStatus})")
                    }
                })
        }
    }

    // Method to toggle the selection state of a contact
    fun selectedContact(contactId: String) {
        if (contactIdList.contains(contactId)) {
            contactIdList.remove(contactId)
        } else {
            contactIdList.add(contactId)
        }
    }

    fun isChecked(contactId: String): Boolean {
        return contactIdList.contains(contactId)
    }

    fun clearData() {
        contactsAdapter = null
        contactIdList.clear()
        _contactsLIst.value = Resource.Loading()
    }

    companion object {
        private const val TAG = "ContactsViewModel"
    }
}