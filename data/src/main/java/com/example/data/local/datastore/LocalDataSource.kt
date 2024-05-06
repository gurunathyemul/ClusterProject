package com.example.data.local.datastore

import com.example.domain.model.contacts.Contact

interface LocalDataSource {
    suspend fun insertContact(name: String?, newNumber: Int?): String
    suspend fun getContacts(query: String?): List<Contact>
    suspend fun fetchAllEmails(displayName: String?): List<Contact>
    suspend fun updateContact(id: String?, newNumber: Int?): String
    suspend fun deleteContact(id: Array<String?>?): String
}