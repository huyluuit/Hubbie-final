/*
 * (C) Copyright 2008 STYL Solutions Pte. Ltd. , All rights reserved
 * This source code and any compilation or derivative thereof is the sole
 * property of STYL Solutions Pte. Ltd. and is provided pursuant to a
 * Software License Agreement.  This code is the proprietary information of
 * STYL Solutions Pte. Ltd. and is confidential in nature. Its use and
 * dissemination by any party other than STYL Solutions Pte. Ltd. is strictly
 * limited by the confidential information provisions of the Agreement
 * referenced above.
 */

package com.example.hubbie.modules.base.view

import androidx.fragment.app.Fragment
import com.example.hubbie.modules.base.BaseContract
import com.example.hubbie.modules.dialog.LoadingFragment
import com.example.hubbie.modules.dialog.MessageDialogFragment

/**
 * Created by trangpham on 16/8/2019.
 */
open class BaseFragment : Fragment(), BaseContract.BaseView, BaseContract.MessageDialogCallbacks {

    override fun showProcessLoading(message: String) {

    }

    private var dialog: MessageDialogFragment? = null

    override fun dismissProcessLoading() {

    }

    private var loadingFragment: LoadingFragment? = null

    override fun onResume() {
        super.onResume()

        dismissLoading()
    }

    override fun showLoading() {
        if (loadingFragment == null) {
            loadingFragment = LoadingFragment().apply {
                isCancelable = false
            }
            fragmentManager?.beginTransaction()
                ?.add(loadingFragment as Fragment, LoadingFragment::class.java.simpleName)
                ?.commitAllowingStateLoss()
        }
    }

    override fun dismissLoading() {
        loadingFragment?.dismiss()
        loadingFragment = null
    }

    override fun showMessage(
        title: String,
        message: String,
        neutralText: String
    ) {
        dialog = MessageDialogFragment.newInstance(title, message, neutralText, this)
        dialog?.isCancelable = false
        fragmentManager?.beginTransaction()?.add(
            (dialog as Fragment),
            MessageDialogFragment::class.java.simpleName
        )?.commit()
    }

    override fun showMessage(
        title: String,
        message: String,
        negativeText: String,
        positiveText: String
    ) {
        dialog = MessageDialogFragment.newInstance(title, message, negativeText, positiveText, this)
        dialog?.isCancelable = false
        fragmentManager?.beginTransaction()?.add(
            (dialog as Fragment),
            MessageDialogFragment::class.java.simpleName
        )?.commit()
    }

    override fun dismissMessage() {
        if (dialog != null) {
            dialog?.dismiss()
        }
    }
}