package com.example.newproject.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.domain.model.contacts.Contact
import com.example.newproject.BR
import com.example.newproject.MainActivity
import com.example.newproject.R
import com.example.newproject.base.BaseFragment
import com.example.newproject.databinding.FragmentContactBinding
import com.example.newproject.other.Resource
import com.example.newproject.util.PermissionUtils
import com.example.newproject.util.PermissionUtils.CONTACT_PERMISSION
import com.example.newproject.viewmodel.ContactsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContactFragment : BaseFragment<MainActivity>() {

    private var contactList: List<Contact> = mutableListOf()
    private lateinit var binding: FragmentContactBinding
    private val contactsViewModel: ContactsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentContactBinding.inflate(inflater, container, false)
        binding.setVariable(BR.mViewModel, contactsViewModel)
        searchViewImpl()
        registerListeners()
        initObservers()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getContacts()
    }

    private fun searchViewImpl() {
        binding.searchContact.apply {
            clearFocus()
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    query?.let { filterList(it) }
                    return false
                }

            })
        }
    }

    override fun registerListeners() {
        super.registerListeners()
        binding.btnDeleteContacts.setOnClickListener {
            if (contactsViewModel.contactIdList.size > 0) {
                contactsViewModel.deleteContacts()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.select_the_contacts),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun initObservers() {
        super.initObservers()
        observeContactList()
    }

    private fun getContacts() {
        if (PermissionUtils.grantedContactPermission(requireContext())) {
            contactsViewModel.getContactsList()
        } else {
            contactsPermission.launch(
                CONTACT_PERMISSION
            )
        }
    }

    private fun observeContactList() {
        lifecycleScope.launch {
            contactsViewModel.contactsList.collect {
                when (it) {
                    is Resource.Loading -> {
                        Log.d(TAG, "onCreate: Loading")
                        binding.apply {
                            pbLoader.visibility = View.VISIBLE
                            rvContact.visibility = View.GONE
                            tvNoContacts.visibility = View.GONE
                            btnDeleteContacts.visibility = View.GONE
                        }
                    }

                    is Resource.Success -> {
                        Log.d(TAG, "onCreate: Success ${it.data}")
                        binding.pbLoader.visibility = View.GONE
                        contactList = it.data ?: emptyList()
                        if (contactList.isNotEmpty()) {
                            binding.rvContact.visibility = View.VISIBLE
                            binding.btnDeleteContacts.visibility = View.VISIBLE
                        } else {
                            binding.tvNoContacts.visibility = View.VISIBLE
                        }
                        contactsViewModel.setContactData(contactList)
                    }

                    is Resource.Error -> {
                        Log.d(TAG, "onCreate: Error ${it.message}")
                        binding.apply {
                            pbLoader.visibility = View.GONE
                            rvContact.visibility = View.GONE
                            tvNoContacts.visibility = View.VISIBLE
                            btnDeleteContacts.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    fun filterList(query: String) {
        val updatedList = contactList.filter { it.name?.contains(query) == true }
        contactsViewModel.setContactData(updatedList)
    }

    private val contactsPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
            if (permission.values.all { it }) {
                contactsViewModel.getContactsList()
            } else {
                Snackbar.make(binding.clContact, "Permission Denied", 6200)
                    .setAction("Setting") {
                        PermissionUtils.navigateToAppPermissionSettings(
                            requireContext()
                        )
                    }
                    .show()
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.clContact.removeAllViews()
        contactList = emptyList()
        contactsViewModel.clearData()
    }

    companion object {
        private const val TAG = "ContactFragment"
    }
}