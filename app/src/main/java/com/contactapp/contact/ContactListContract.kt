package com.contactapp.contact

import com.contactapp.contact.model.Contact
import com.scancriteria.base.BaseContract

class ContactListContract {
    interface ContactListView : BaseContract.BaseView {
        fun showProgressbar()
        fun hideProgressbar()
        fun showContactList(contactList: List<Contact>)
    }

    interface ContactListUserActionListener : BaseContract.BaseUserActionsListener {
        fun loadContactList()
        fun onStop()
    }
}
