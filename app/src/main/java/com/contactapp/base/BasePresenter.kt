package com.scancriteria.base

import java.lang.ref.WeakReference

abstract class BasePresenter<V : BaseContract.BaseView> : BaseContract.BaseUserActionsListener {

    private var mView: WeakReference<V>? = null

    override fun bindView(view: BaseContract.BaseView) {
        this.mView = WeakReference(view as V)
    }

    override fun unbindView() {
        this.mView = null
    }

    protected fun view(): V? {
        return if (mView == null)
            null
        else
            mView?.get()
    }
}