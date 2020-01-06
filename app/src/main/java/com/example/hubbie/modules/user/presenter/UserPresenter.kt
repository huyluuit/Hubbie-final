package com.example.hubbie.modules.user.presenter

import android.util.Log
import com.example.hubbie.entities.User
import com.example.hubbie.entities.shared.AccountPreferences
import com.example.hubbie.modules.user.IUser
import com.example.hubbie.modules.user.interactor.UserInteractor
import com.example.hubbie.modules.user.view.UserFragment

class UserPresenter(private val framgent: UserFragment) : IUser.Presenter,
    IUser.Interactor.Callbacks {

    private var interactor: UserInteractor? = UserInteractor(this)
    private var view: IUser.View? = null
    private val userList = ArrayList<User>()

    override fun doUserListChangeListener() {
        val baseAccount = AccountPreferences(framgent.context!!).getBaseAccount()
        interactor?.doUserListChange(baseAccount.uid ?: "")
    }

    override fun setView(view: IUser.View) {
        this.view = view
    }

    override fun onUpdateUserItem(item: User) {
        var p = 0
        for (user in userList) {
            if (item.uid == user.uid) {
                break
            }
            p++
        }
        userList.set(p, item)
        view?.onUserUpdate(p)
    }

    override fun onAddUserItem(item: User) {
        userList.add(item)
        view?.onNewUserAdd(userList.lastIndex)
    }

    override fun onUserDelete(item: User) {
        var p = 0
        for (user in userList) {
            if (item.uid == user.uid) {
                break
            }
            p++
        }
        userList.remove(item)
        view?.onUserDelete(p)
    }

    override fun onBaseUser(baseUser: ArrayList<User>) {
        this.userList.addAll(baseUser)
        view?.setBaseUser(this.userList)
    }

    override fun getAllUsers() {
        val baseAccount = AccountPreferences(framgent.context!!).getBaseAccount()
        interactor?.getAllUsers(baseAccount)
    }

    override fun onDestroy() {
        interactor = null
    }

}