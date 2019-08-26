package com.contactapp.contact

import com.contactapp.WebServicesCallback
import com.contactapp.contact.model.Contact
import com.contactapp.contact.model.ContactListApiImp
import com.scancriteria.base.BasePresenter

class ContactListPresenter(contactListApiImp: ContactListApiImp) : BasePresenter<ContactListContract.ContactListView>(),
    ContactListContract.ContactListUserActionListener {
    val contactListApi = contactListApiImp

    override fun loadContactList() {
        view()?.showProgressbar()
        contactListApi.loadContactLis(object : WebServicesCallback<List<Contact>> {
            override fun onSuccess(contactList: List<Contact>) {
                view()?.hideProgressbar()
                //Sort  in alphabatic order
                val sortList = contactList.sortedBy { contact -> contact.name }
                view()?.showContactList(sortList)
            }

            override fun onFailure(errorMsg: String) {
                view()?.hideProgressbar()
                view()?.showErrorMsg(errorMsg)
            }

            override fun onDefaultError() {
                view()?.hideProgressbar()
                view()?.showDefaultError()
            }
        })
    }

    override fun onStop() {
        contactListApi.onCancel()
    }
}
