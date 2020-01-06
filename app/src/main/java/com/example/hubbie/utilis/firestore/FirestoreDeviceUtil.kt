package com.example.hubbie.utilis.firestore

import android.util.Log
import com.example.hubbie.entities.Device
import com.example.hubbie.entities.DeviceSorted
import com.example.hubbie.utilis.realtime.FirebaseRealtimeDevice
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy

object FirestoreDeviceUtil {

    const val COLLECTION_DEVICE_LIST = "DeviceList"
    const val DEVICE_ADDED = com.example.hubbie.BuildConfig.APPLICATION_ID + ".DEVICE_HAD_ADDED"
    const val DEVICE_UPDATED = com.example.hubbie.BuildConfig.APPLICATION_ID + ".DEVICE_HAD_UPDATED"
    const val DEVICE_REMOVED = com.example.hubbie.BuildConfig.APPLICATION_ID + ".DEVICE_HAD_REMOVED"
    private val db = FirebaseFirestore.getInstance().collection(FirestoreUserUtil.COLLECTION_USER)

    fun getAllDeviceList(uid: String): Single<Pair<ArrayList<Device>, ArrayList<DeviceSorted>>> {
        return Single.create {
            db.document(uid).collection(COLLECTION_DEVICE_LIST).get()
                .addOnCompleteListener { task ->

                    if (task.isSuccessful && task.result != null) {
                        val result = ArrayList<String>()
                        result.clear()
                        for (item in task.result!!.documents) {
                            val deviceSorted = item.data!!["macAddress"].toString()
                            result.add(deviceSorted)
                        }
                        Log.e("HuyHuy", "DeviceList: $result")
                        FirebaseRealtimeDevice.getBaseDevice(result)
                            .observeOn(AndroidSchedulers.mainThread()).subscribeBy(
                                onSuccess = { it2 ->
                                    it.onSuccess(it2)
                                }
                            )
                    } else {
                        return@addOnCompleteListener
                    }

                }
        }

    }

    fun setDevice(uid: String, device: DeviceSorted): Completable {

        return Completable.create {

            db.document(uid).collection(COLLECTION_DEVICE_LIST).document(device.macAddress)
                .set(device).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        it.onComplete()
                    } else {
                        return@addOnCompleteListener
                    }
                }
        }

    }

    private var isFirstRun = false
    fun doEventChangeListener(userId: String): Observable<Pair<String, Device>> {
        return Observable.create {
            db.document(userId).collection(COLLECTION_DEVICE_LIST)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        return@addSnapshotListener
                    }

                    if (querySnapshot != null) {
                        if (isFirstRun) {
                            for (item in querySnapshot.documentChanges) {
                                var type = ""
                                when (item.type) {
                                    DocumentChange.Type.ADDED -> {
                                        type = DEVICE_ADDED
                                    }
                                    DocumentChange.Type.MODIFIED -> {
                                        type = DEVICE_UPDATED
                                    }
                                    DocumentChange.Type.REMOVED -> {
                                        type = DEVICE_REMOVED
                                    }
                                }
                                FirebaseRealtimeDevice.getDevice(item.document.data["macAddress"].toString())
                                    .observeOn(AndroidSchedulers.mainThread()).subscribeBy(
                                        onSuccess = { it2 ->
                                            it.onNext(Pair(type, it2))
                                        },
                                        onError = {

                                        }
                                    )
                            }
                        } else {
                            isFirstRun = true
                        }
                    }
                }
        }
    }
}