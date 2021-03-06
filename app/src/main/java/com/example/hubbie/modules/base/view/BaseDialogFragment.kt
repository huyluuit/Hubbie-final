package com.example.hubbie.modules.base.view

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.hubbie.modules.base.BaseContract
import com.example.hubbie.modules.dialog.LoadingFragment
import com.example.hubbie.modules.dialog.MessageDialogFragment
import com.example.hubbie.modules.dialog.ProcessDialog

open class BaseDialogFragment : DialogFragment(), BaseContract.BaseView,
    BaseContract.MessageDialogCallbacks {

    private var loadingFragment: LoadingFragment? = null
    private var processLoading: ProcessDialog? = null
    private var messageDialogFragment: MessageDialogFragment? = null


    override fun showMessage(
        title: String,
        message: String,
        neutralText: String
    ) {
        messageDialogFragment = MessageDialogFragment.newInstance(title, message, neutralText, this)
        messageDialogFragment?.isCancelable = false
        fragmentManager?.beginTransaction()?.add(
            (messageDialogFragment as Fragment) ,
            MessageDialogFragment::class.java.simpleName
        )?.commitAllowingStateLoss()
    }

    override fun showMessage(
        title: String,
        message: String,
        negativeText: String,
        positiveText: String
    ) {
        messageDialogFragment = MessageDialogFragment.newInstance(title, message, negativeText, positiveText, this)
        messageDialogFragment?.isCancelable = false
        fragmentManager?.beginTransaction()?.add(
            (messageDialogFragment as Fragment) ,
            MessageDialogFragment::class.java.simpleName
        )?.commitAllowingStateLoss()
    }

    override fun dismissMessage() {
        if(messageDialogFragment != null){
            messageDialogFragment?.dismiss()
            messageDialogFragment = null
        }
    }


    override fun onResume() {
        loadingFragment?.dismiss()
        super.onResume()
    }

    override fun showProcessLoading(message: String) {
        if (processLoading == null) {
            processLoading = ProcessDialog(message)
            fragmentManager?.beginTransaction()
                ?.add(processLoading as Fragment, ProcessDialog::class.java.simpleName)
                ?.commitAllowingStateLoss()
        }
    }

    override fun dismissProcessLoading() {
        processLoading?.dismissAllowingStateLoss()
        processLoading = null
    }

    override fun showLoading() {
        if (loadingFragment == null) {
            loadingFragment = LoadingFragment()
            fragmentManager?.beginTransaction()
                ?.add(loadingFragment as Fragment, LoadingFragment::class.java.simpleName)
                ?.commitAllowingStateLoss()
        }
    }

    override fun dismissLoading() {
        loadingFragment?.dismissAllowingStateLoss()
        loadingFragment = null
    }

}