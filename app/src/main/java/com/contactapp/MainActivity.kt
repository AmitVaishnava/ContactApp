package com.contactapp

import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.contactapp.contact.ContactListFragment
import com.contactapp.contact.model.Contact
import com.contactapp.contactdetail.ContactDetailsFragment
import com.contactapp.utils.FragmentHelper

class MainActivity : AppCompatActivity(), ContactListFragment.ContactListFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.applicationContext.contentResolver
            .registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, myContentObserver)

        FragmentHelper.replace(this, R.id.parent_layout, ContactListFragment.newInstance())
    }

    //Content Obsever user to listen data change in Native Contact App
    //TODO:Another way is to use sync adapter, but I hasd used ContactObserver
    var myContentObserver = object : ContentObserver(null) {
        override fun onChange(selfChange: Boolean, uri: Uri?) {
            super.onChange(selfChange, uri)
        }
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            val fragment = FragmentHelper.get(this@MainActivity, R.id.parent_layout)
            if (fragment is ContactListFragment) {
                fragment.forceUpdate()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        applicationContext.contentResolver.unregisterContentObserver(myContentObserver)
    }

    override fun onItemClick(contact: Contact) {
        FragmentHelper.addAddToBackStack(
            this,
            R.id.parent_layout,
            ContactDetailsFragment.newInstance(contact),
            ContactDetailsFragment.javaClass.name
        )
    }
}
