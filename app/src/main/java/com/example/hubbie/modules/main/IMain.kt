package com.example.hubbie.modules.main

import com.example.hubbie.entities.Device
import com.example.hubbie.entities.Room
import com.example.hubbie.entities.User
import com.example.hubbie.modules.base.BaseContract

interface IMain {
    interface Presenter : BaseContract.BasePresenter {
        fun setView(view: View)
        fun onMyInfoClicked()
        fun onChatClick()
        fun logOutClicked()
        fun createRoomClicked()
        fun onNewRoomCreated(room: Room)
        fun doUserChangeListener(userId: String)
        fun onItemRoomClicked(room: Room)
        fun onItemUserClicked(user: User)
        fun onItemDeviceClicked(device: Device)
    }

    interface Interactor : BaseContract.BaseInteractor {
        fun addNewRoom(room: Room)
        fun doAccountChangeListener(userId: String)
        interface Callbacks {
            fun onAccountDelete()
            fun onAccountActivateChanges(state: Boolean)
        }
    }

    interface Route : BaseContract.BaseRouter {
        fun navigateToLogin()
        fun navigateToAccountInfo()
        fun navigateToRoomDetail(room: Room)
        fun navigateToUserDetail(user: User)
        fun navigateToDeviceDetail(device: Device)
        fun navigateToChat()
    }

    interface View {
        fun onRoomItemClicked(room: Room)
        fun onUserItemClicked(user: User)
        fun onDeviceItemClicked(device: Device)
        fun accountActiveChangeState(result: Boolean)
    }
}