package com.example.hubbie.utilis.realtime

import com.example.hubbie.entities.Device
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Completable
import io.reactivex.Single

object FirebaseRealtimeDevice {

    private val db = FirebaseDatabase.getInstance()
    private val ref = db.getReference("MAC")

    fun getBaseDevice(): Single<ArrayList<Device>> {
        return Single.create {

        }
    }

    fun setDevice(device: Device): Completable {
        return Completable.create {

        }
    }

    fun getDevice(deviceId: String): Single<Device> {
        return Single.create {

        }
    }
}