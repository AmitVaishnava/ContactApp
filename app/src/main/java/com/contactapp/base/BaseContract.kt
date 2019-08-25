package com.scancriteria.base

class BaseContract {
    //Base interface for View
    interface BaseView {
        fun showNetworkError()
        fun showErrorMsg(errorMsg: String)
        fun showDefaultError()
    }

    //Base interface for Presenter
    interface BaseUserActionsListener {
        fun bindView(view: BaseView)
        fun unbindView()
    }
}