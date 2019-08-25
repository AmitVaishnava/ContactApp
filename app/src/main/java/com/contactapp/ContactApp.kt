package com.contactapp

import android.app.Application

class ContactApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: ContactApp
    }
}
