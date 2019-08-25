package com.contactapp.contact.model

import com.contactapp.WebServicesCallback

interface ContactListApi {
    fun loadContactLis(data: WebServicesCallback<List<Contact>>)

    fun onCancel()
}
