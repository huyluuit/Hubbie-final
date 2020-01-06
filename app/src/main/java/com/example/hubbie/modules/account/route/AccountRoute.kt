package com.example.hubbie.modules.account.route

import android.content.Context
import com.example.hubbie.MainActivity
import com.example.hubbie.modules.account.IAccount
import com.example.hubbie.modules.login.view.LoginFragment

class AccountRoute : IAccount.Route {
    override fun navigateToMain() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun navigationLogin(context: Context){
        val fragment = LoginFragment()
        (context as MainActivity).changeFragment(fragment)
    }

}