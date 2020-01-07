package com.example.hubbie.modules.account

import android.content.Context
import com.example.hubbie.entities.User

interface IAccount {
    interface Presenter {
        fun pwdChangeClicked(newPwd: String)
        fun phoneChangeClicked(newPhone: String)
        fun nameDisplayChangeClicked(newNameDisplay: String)
        fun logOut(uid: String, context: Context)
        fun getUser(): User?
    }

    interface Interactor {
        fun insertUser(user: User): Boolean
        fun getUser(userId: Int): User
    }

    interface Route {
        fun navigateToMain()
        fun navigationLogin(context: Context)
    }

    interface View
}