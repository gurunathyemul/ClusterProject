package com.example.domain.usecase.contacts

import com.example.domain.base.UseCase
import com.example.domain.exceptions.IErrorHandler
import com.example.domain.repositories.ContactsRepository
import javax.inject.Inject

class DeleteContactsUseCase @Inject constructor(
    private val contactsRepository: ContactsRepository,
    errorHandler: IErrorHandler
) : UseCase<String, Array<String?>?>(errorHandler) {

    override suspend fun run(params: Array<String?>?): String =
        contactsRepository.deleteContact(params)
}