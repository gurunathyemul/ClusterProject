package com.example.data.local.datastore

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.database.DatabaseUtils
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import com.example.domain.model.contacts.Contact

class LocalDataSourceImpl(private val contentResolver: ContentResolver) : LocalDataSource {

    override suspend fun insertContact(name: String?, newNumber: Int?): String {
        val values = ContentValues().apply {
            put(ContactsContract.RawContacts.ACCOUNT_TYPE, "")
            put(ContactsContract.RawContacts.ACCOUNT_NAME, "")
        }
        val rawContactUri: Uri? =
            contentResolver.insert(ContactsContract.RawContacts.CONTENT_URI, values)
        val rawContactId = rawContactUri?.let {
            ContentUris.parseId(it)
        }
        values.apply {
            clear()
            put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
            put(
                ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
            )
            put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
        }
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, values);

        val phoneValues = ContentValues().apply {
            put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
            put(
                ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
            )
            put(ContactsContract.CommonDataKinds.Phone.NUMBER, newNumber)
            put(
                ContactsContract.CommonDataKinds.Phone.TYPE,
                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
            )
        }

        // Insert the contact's phone number
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, phoneValues)

        val emailValues = ContentValues().apply {
            put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
            put(
                ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE
            )
            put(ContactsContract.CommonDataKinds.Email.ADDRESS, "a@gmail.com");
            put(
                ContactsContract.CommonDataKinds.Email.TYPE,
                ContactsContract.CommonDataKinds.Email.TYPE_OTHER
            )
        }

        // Insert the contact's phone number
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, emailValues)
        return "Contact Inserted Successfully"
    }

    override suspend fun getContacts(query: String?): List<Contact> {
        val contactList = mutableListOf<Contact>()
        val contactsProjection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
        )
        //query desired for a contact
        val mCursor =
            contentResolver.query(
                /* uri = */ ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                /* projection = */
                contactsProjection,
                /* selection = */
                query?.let { "${ContactsContract.Contacts.DISPLAY_NAME} LIKE '%$query%'" },
                /* selectionArgs = */
                null,
                /* sortOrder = */
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
            )
        val stringCursor = DatabaseUtils.dumpCursorToString(mCursor)
        Log.d(TAG, "getContacts::$stringCursor")
        if (mCursor != null)
            with(mCursor) {
                val displayNameIndex =
                    getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val photoUriIndex =
                    getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)
                val numberIndex =
                    getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val contactId =
                    getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)

                while (moveToNext()) {
                    contactList.add(
                        Contact(
                            name = getString(displayNameIndex),
                            image = getString(photoUriIndex),
                            number = getString(numberIndex),
                            contactId = getString(contactId),
                        )
                    )
                }
            }
        mCursor?.close()
        return contactList
    }

    /**
     * Fetching all the emails of the contact sorted by type.
     */
    override suspend fun fetchAllEmails(displayName: String?): List<Contact> {
        val emailsList: MutableList<Contact> = mutableListOf()
        val emailProjection = arrayOf(
            ContactsContract.CommonDataKinds.Email.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Email.ADDRESS,
            ContactsContract.CommonDataKinds.Email.TYPE,
            ContactsContract.CommonDataKinds.Email.RAW_CONTACT_ID,
            ContactsContract.CommonDataKinds.Email._ID
        )
        val mCursor = contentResolver.query(
            /* uri = */ ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            /* projection = */ emailProjection,
            /* selection = */ "${ContactsContract.Contacts.DISPLAY_NAME} = '${displayName}'",
            /* selectionArgs = */ null,
            /* sortOrder = */ ContactsContract.CommonDataKinds.Email.TYPE
        )

        // display of the provider database for debugging purposes.
        val dCursor = DatabaseUtils.dumpCursorToString(mCursor)
        Log.d("ContactsDataSource", dCursor)

        mCursor?.apply {
            while (moveToNext()) {
                val emailIndex = getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
                val emailTypeIndex = getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE)
                emailsList.add(
                    Contact(
                        email = getString(emailIndex), emailType = getString(emailTypeIndex)
                    )
                )
            }
        }
        mCursor?.close()
        return emailsList
    }

    override suspend fun updateContact(id: String?, newNumber: Int?): String {
        val contentValues = ContentValues().apply {
            put(ContactsContract.CommonDataKinds.Phone.NUMBER, newNumber)
        }
        contentResolver.update(
            ContactsContract.Data.CONTENT_URI,
            contentValues,
            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",//updating on single item
            arrayOf(id)
        )
        return "Contact Updated Successfully"
    }

    override suspend fun deleteContact(id: Array<String?>?): String {
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            id?.let { "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} IN (${Array(id.size) { "?" }.joinToString()})" },
            id,
            null
        )
        while (cursor!!.moveToNext()) {
            try {
                val lookupKey =
                    cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.LOOKUP_KEY))
                val uri =
                    Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey)
                contentResolver.delete(uri, null, null)
            } catch (e: Exception) {
                println(e.stackTrace)
                return e.message ?: ""
            }
        }
        cursor.close()
        return "Contacts deleted successfully"
    }

    companion object {
        private const val TAG = "LocalDataSourceImpl"
    }
}