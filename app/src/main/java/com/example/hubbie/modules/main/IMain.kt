package com.example.hubbie.modules.main

import com.example.hubbie.entities.Control
import com.example.hubbie.entities.Room
import com.example.hubbie.modules.base.BaseContract

interface IMain {
    interface Presenter: BaseContract.BasePresenter {
        fun itemControlClicked(controlId: Int)
        fun roomItemCliked(roomId: Int)
        fun createRoomClicked()
        fun getAllRooms(): ArrayList<String>
    }

    interface Interactor: BaseContract.BaseInteractor{
        fun getRoom(roomId: Int): Room
        fun getAllControls(roomId: Int): ArrayList<Control>
        fun getAllRooms(): ArrayList<Room>
    }

    interface Route: BaseContract.BaseRouter {
        fun navigateToLogin()
        fun navigateToAccountInfo()
        fun navigateToCreateRoom()
        fun navigateToRoomDetail()
        fun navigateToChat()
    }

    interface View {
        fun onItemClicked()
        fun createRoomClicked()
    }
}