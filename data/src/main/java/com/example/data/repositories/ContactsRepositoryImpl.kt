package com.example.data.repositories

import com.example.data.local.datastore.LocalDataSource
import com.example.domain.model.contacts.Contact
import com.example.domain.repositories.ContactsRepository
import javax.inject.Inject

class ContactsRepositoryImpl @Inject constructor(private val localDataSource: LocalDataSource) :
    ContactsRepository {
    override suspend fun insertContact(name: String?, newNumber: Int?): String =
        localDataSource.insertContact(name, newNumber)

    override suspend fun getContacts(query: String?): List<Contact> =
        localDataSource.getContacts(query)

    override suspend fun fetchAllEmails(displayName: String?): List<Contact> =
        localDataSource.fetchAllEmails(displayName)

    override suspend fun updateContact(id: String?, newNumber: Int?): String =
        localDataSource.updateContact(id, newNumber)

    override suspend fun deleteContact(id: Array<String?>?): String =
        localDataSource.deleteContact(id)

}