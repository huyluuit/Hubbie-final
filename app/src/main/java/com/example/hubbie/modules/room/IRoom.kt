package com.example.hubbie.modules.room

import com.example.hubbie.entities.Control
import com.example.hubbie.entities.Room

interface IRoom {
    interface Presenter {
        fun itemControlClicked(controlId: Int)
        fun roomItemCliked(roomId: Int)
        fun createRoomClicked()
        fun getAllRooms(): ArrayList<String>
    }

    interface Interactor {
        fun getRoom(roomId: Int): Room
        fun getAllControls(roomId: Int): ArrayList<Control>
        fun getAllRooms(): ArrayList<Room>
    }

    interface Route {
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