package com.example.hubbie.modules.base

interface BaseContract {

    interface MessageDialogCallbacks{
        fun onNeutralClick(){}
        fun onNegativeClick(){}
        fun onPositiveClick(){}
    }

    interface BaseView {
        fun showLoading()
        fun showMessage(title: String, message: String, neutralText: String)
        fun showMessage(title: String, message: String, negativeText: String, positiveText: String)
        fun dismissMessage()
        fun dismissLoading()
        fun showProcessLoading(message: String)
        fun dismissProcessLoading()
    }

    interface BasePresenter {
        fun onDestroy()
    }

    interface BaseInteractor {
        fun onDestroy()
    }

    interface BaseRouter

}