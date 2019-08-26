package com.contactapp.contact.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Contact(val id:String?,var name: String?, var number: String?, var emailAddress: String?, var imagePath: String?) : Parcelable
