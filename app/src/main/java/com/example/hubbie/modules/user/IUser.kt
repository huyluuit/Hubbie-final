package com.example.hubbie.modules.user

import com.example.hubbie.entities.BaseUser
import com.example.hubbie.entities.User
import com.example.hubbie.modules.base.BaseContract

interface IUser {

    interface Presenter: BaseContract.BasePresenter {
        fun setView(view: View)
        fun doUserListChangeListener()
        fun getAllUsers()
    }

    interface Interactor: BaseContract.BaseInteractor {

        fun getAllUsers(user: User)

        fun doUserListChange(userId: String)

        interface Callbacks {
            fun onBaseUser(baseUser: ArrayList<User>)
            fun onUpdateUserItem(item: User)
            fun onAddUserItem(item: User)
            fun onUserDelete(item: User)
        }
    }

    interface View{
        fun setBaseUser(userList: ArrayList<User>)
        fun onNewUserAdd(position: Int)
        fun onUserUpdate(position: Int)
        fun onUserDelete(position: Int)
        fun onBaseUserList(baseUserList: ArrayList<BaseUser>)
    }
}