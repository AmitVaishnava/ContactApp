package com.contactapp.utils

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.contactapp.ContactApp

object UiUtils {
    fun getBitmapFromPath(contactId: String): Bitmap? {
        return MediaStore.Images.Media
            .getBitmap(
                ContactApp.instance.getContentResolver(),
                Uri.parse(contactId)
            )
    }
}
