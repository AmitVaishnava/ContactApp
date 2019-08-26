package com.contactapp.contactdetail

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.telephony.SmsManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.contactapp.R
import com.contactapp.contact.model.Contact
import com.contactapp.utils.Constant
import com.contactapp.utils.UiUtils
import com.scancriteria.base.BaseContract
import com.scancriteria.base.BaseFragment

class ContactDetailsFragment : BaseFragment<BaseContract.BaseUserActionsListener>(),
    BaseContract.BaseView, View.OnClickListener {

    private var mContact: Contact? = null
    private lateinit var mProfileImageView: ImageView
    private lateinit var mNameTextView: TextView
    private lateinit var mNumberTextView: TextView
    private lateinit var mEmailAddressTextView: TextView
    private lateinit var mCallImageButton: ImageButton
    private lateinit var mSmsImageButton: ImageButton

    companion object {
        fun newInstance(contact: Contact): ContactDetailsFragment {
            val args = Bundle()
            val fragment = ContactDetailsFragment()
            args.putParcelable("contact_details", contact)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.contact_detail, container, false)
    }

    override fun initUserActionListener() {
        if (arguments != null) {
            mContact = arguments?.getParcelable("contact_details")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mProfileImageView = view.findViewById(R.id.profile_img)
        mNameTextView = view.findViewById(R.id.name_txt_view)
        mNumberTextView = view.findViewById(R.id.number_txt_view)
        mEmailAddressTextView = view.findViewById(R.id.email_txt_view)
        mCallImageButton = view.findViewById(R.id.call_img_btn)
        mCallImageButton.setOnClickListener(this)
        mSmsImageButton = view.findViewById(R.id.sms_img_btn)
        mSmsImageButton.setOnClickListener(this)

        mCallImageButton.isEnabled = false//initially disable
        mSmsImageButton.isEnabled = false

        initData()
    }

    private fun initData() {
        mContact?.let {
            if (!TextUtils.isEmpty(mContact?.imagePath))
                mProfileImageView.setImageBitmap(mContact?.imagePath?.let { it ->
                    UiUtils.getBitmapFromPath(it)
                })

            mNameTextView.text = mContact?.name
            mNumberTextView.text = mContact?.number
            mEmailAddressTextView.text = mContact?.emailAddress

            if (!TextUtils.isEmpty(mContact?.number)) {//If number is not empty then call and SMS event happen
                mCallImageButton.isEnabled = true
                mSmsImageButton.isEnabled = true
            }
        }
    }

    override fun onClick(view: View?) {
        if (TextUtils.isEmpty(mContact?.number)) {
            showErrorMsg("Invalid Ph Number")
            return
        }
        if (view?.id == R.id.call_img_btn) {
            callAction()
        } else if (view?.id == R.id.sms_img_btn) {
            smsClick()
        }
    }

    private fun smsClick() {
        if (checkPermission()) {
            smsAction()
        } else {
            requestPermission()
        }
    }

    private fun smsAction() {
        val smsString = "sample text"
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(mContact?.number, null, smsString, null, null)
        Toast.makeText(context, "SMS send Please check your inbox", Toast.LENGTH_LONG).show()
    }

    private fun checkPermission(): Boolean {
        val result = context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.SEND_SMS) }
        return if (result == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            false
        }
    }

    private fun callAction() {
        val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", mContact?.number, null))
        startActivity(intent)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.SEND_SMS),
            Constant.PERMISSION_REQUEST_CODE
        )

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constant.PERMISSION_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                smsAction()
            } else {
                showErrorMsg("Permission denied")
            }
        }
    }
}
