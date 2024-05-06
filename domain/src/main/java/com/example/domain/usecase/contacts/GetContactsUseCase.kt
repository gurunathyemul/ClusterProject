package com.example.domain.usecase.contacts

import com.example.domain.base.UseCase
import com.example.domain.exceptions.IErrorHandler
import com.example.domain.model.contacts.Contact
import com.example.domain.repositories.ContactsRepository
import javax.inject.Inject

class GetContactsUseCase @Inject constructor(
    private val contactsRepository: ContactsRepository,
    errorHandler: IErrorHandler
) : UseCase<List<Contact>, String>(errorHandler) {

    override suspend fun run(params: String?): List<Contact> =
        contactsRepository.getContacts(params)
}