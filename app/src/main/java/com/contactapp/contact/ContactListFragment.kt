package com.contactapp.contact

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.contactapp.R
import com.contactapp.contact.model.Contact
import com.contactapp.contact.model.ContactListApiImp
import com.contactapp.utils.Constant
import com.scancriteria.base.BaseFragment


class ContactListFragment : BaseFragment<ContactListContract.ContactListUserActionListener>(),
    ContactListContract.ContactListView, ContactListAdapter.ContactListAdapterListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: View
    private var mContactList: List<Contact>? = null
    private lateinit var contactListAdapter: ContactListAdapter
    private lateinit var contactListFragmentListener: ContactListFragmentListener
    private var isForceUpdate = false

    companion object {
        fun newInstance(): ContactListFragment {
            return ContactListFragment()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ContactListFragmentListener)
            contactListFragmentListener = context
    }

    override fun initUserActionListener() {
        mUserActionListener = ContactListPresenter(ContactListApiImp())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.recycler_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = view.findViewById(R.id.progress_bar)
        recyclerView = view.findViewById(R.id.recycler_view)

        contactListAdapter = ContactListAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = contactListAdapter
    }

    override fun onResume() {
        super.onResume()
        if (mContactList.isNullOrEmpty() || isForceUpdate) {
            isForceUpdate = false
            handleContactListData()
        }
    }

    fun handleContactListData() {
        if (checkPermission()) {
            loadContactList()
        } else {
            requestPermission()
        }
    }

    fun forceUpdate(){
        isForceUpdate= true
    }
    fun loadContactList() {
        mUserActionListener?.loadContactList()
    }

    override fun onStop() {
        super.onStop()
        mUserActionListener?.onStop()
    }

    fun checkPermission(): Boolean {
        val CallPermissionResult =
            context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.CALL_PHONE) }
        val ContactPermissionResult =
            context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CONTACTS) }
        return CallPermissionResult == PackageManager.PERMISSION_GRANTED && ContactPermissionResult == PackageManager.PERMISSION_GRANTED

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constant.PERMISSION_REQUEST_CODE) {
            if (grantResults.size > 0) {
                val CallPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                val ReadContactsPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED

                if (CallPermission && ReadContactsPermission) {
                    loadContactList()
                } else {
                    showErrorMsg("Permission denied")
                }
            }
        }
    }

    private fun requestPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE),
            Constant.PERMISSION_REQUEST_CODE
        )

    }

    override fun showProgressbar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressbar() {
        progressBar.visibility = View.GONE
    }

    override fun showContactList(contactList: List<Contact>) {
        mContactList = contactList
        contactListAdapter.contactList = contactList
    }

    override fun onItemClick(position: Int) {
        val contact = mContactList?.get(position)
        contact?.let { contactListFragmentListener.onContactListItemClick(it) }
    }

    interface ContactListFragmentListener {
        fun onContactListItemClick(contact: Contact)
    }
}
