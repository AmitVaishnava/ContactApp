package com.contactapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.contactapp.contact.ContactListFragment
import com.contactapp.contact.model.Contact
import com.contactapp.contactdetail.ContactDetailsFragment
import com.contactapp.utils.FragmentHelper

class MainActivity : AppCompatActivity(), ContactListFragment.ContactListFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FragmentHelper.replace(this, R.id.parent_layout, ContactListFragment.newInstance())
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
