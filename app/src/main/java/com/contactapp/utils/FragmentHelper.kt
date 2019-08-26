package com.contactapp.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

object FragmentHelper {

    fun replace(activity: FragmentActivity, container: Int, fragment: Fragment) {
        val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(container, fragment).commit()
    }

    fun addAddToBackStack(activity: FragmentActivity, container: Int, fragment: Fragment, tag: String) {
        val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
        fragmentTransaction.add(container, fragment)
            .addToBackStack(tag)
            .commit()
    }

    fun get(activity: FragmentActivity, container: Int): Fragment? {
        return activity.supportFragmentManager.findFragmentById(container)
    }
}
