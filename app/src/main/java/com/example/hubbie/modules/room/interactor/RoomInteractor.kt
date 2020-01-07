package com.example.hubbie.modules.room.interactor

import android.util.Log
import com.example.hubbie.entities.shared.AccountPreferences
import com.example.hubbie.modules.room.IRoom
import com.example.hubbie.modules.room.view.RoomFragment
import com.example.hubbie.utilis.firestore.FirestoreRoomUtil
import com.example.hubbie.utilis.realtime.FirebaseRealtimeDevice
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class RoomInteractor(
    private val fragment: RoomFragment,
    private val callbacks: IRoom.Interactor.Callbacks
) :
    IRoom.Interactor {
    override fun onDestroy() {
        compositeDisposable.dispose()
    }

    private val compositeDisposable = CompositeDisposable()

    override fun getBaseRooms() {
        val baseUser = AccountPreferences(fragment.context!!).getBaseAccount()
        val roomDisposable = FirestoreRoomUtil.getBaseRoom(baseUser).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribeBy(
                onSuccess = {
                    callbacks.onBaseRoom(it)
                },
                onError = {

                }
            )

        compositeDisposable.add(roomDisposable)
    }

    override fun doRoomChangeListener() {
        val baseUser = AccountPreferences(fragment.context!!).getBaseAccount()
        val roomListener =
            FirestoreRoomUtil.doRoomListener(baseUser.uid ?: "").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = {
                        Log.e("Test", "Room Modified: " + it.second)
                        when (it.first) {
                            FirestoreRoomUtil.ROOM_ADDED -> callbacks.onRoomAdded(it.second)
                            FirestoreRoomUtil.ROOM_UPDATED -> callbacks.onRoomAdded(it.second)
                            FirestoreRoomUtil.ROOM_REMOVED -> callbacks.onRoomAdded(it.second)
                        }
                    },
                    onError = {

                    }
                )
        compositeDisposable.addAll(roomListener)
    }

    override fun onSw1Change(deviceId: String, state: Boolean) {
        FirebaseRealtimeDevice.setPortStateA(deviceId, state)
    }

    override fun onSw2Change(deviceId: String, state: Boolean) {
        FirebaseRealtimeDevice.setPortStateB(deviceId, state)
    }

    override fun onSw3Change(deviceId: String, state: Boolean) {
        FirebaseRealtimeDevice.setPortStateC(deviceId, state)
    }

}