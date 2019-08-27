package com.contactapp.contact.model

import android.os.AsyncTask
import android.provider.ContactsContract
import android.text.TextUtils
import com.contactapp.ContactApp
import com.contactapp.WebServicesCallback

class ContactListApiImp : ContactListApi {
    private lateinit var asyncTask: AsyncTask<String, String, List<Contact>>

    override fun loadContactLis(data: WebServicesCallback<List<Contact>>) {
        asyncTask = object : AsyncTask<String, String, List<Contact>>() {
            override fun doInBackground(vararg strings: String): List<Contact> {
                return getContactList()
            }

            override fun onPostExecute(result: List<Contact>?) {
                super.onPostExecute(result)
                if (result != null) {
                    data.onSuccess(result)
                } else {
                    data.onFailure("data not found")
                }
            }
        }.execute()
    }

    private fun getContactList(): List<Contact> {
        var contactList: MutableList<Contact> = arrayListOf()
        val contentResolver = ContactApp.instance.contentResolver
        val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

        cursor?.let {
            if (cursor?.getCount() > 0) {
                while (cursor?.moveToNext()) {
                    var emailAddress = ""
                    var phone = ""
                    val id = cursor?.getString(cursor?.getColumnIndex(ContactsContract.Contacts._ID))
                    val name = cursor?.getString(cursor?.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val photoUri =
                        cursor?.getString(cursor?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
                    if (Integer.parseInt(
                            cursor?.getString(
                                cursor?.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                            )
                        ) > 0
                    ) {
                        val phoneCur = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf<String>(id), null
                        )
                        phoneCur?.let {
                            while (phoneCur?.moveToNext()) {
                                phone =
                                    phoneCur?.getString(phoneCur?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                            }
                            phoneCur?.close()
                        }

                        val emailCur = contentResolver.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            arrayOf<String>(id), null
                        )
                        emailCur?.let {
                            while (emailCur?.moveToNext()) {
                                emailAddress =
                                    emailCur?.getString(emailCur?.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))

                            }
                            emailCur?.close()
                        }

                        if (!TextUtils.isEmpty(phone))
                            contactList?.add(Contact(id,name, phone, emailAddress, photoUri))
                    }
                }
            }
        }
        return contactList
    }

    override fun onCancel() {
        if (::asyncTask.isInitialized) {
            asyncTask.cancel(true)
        }
    }
}
