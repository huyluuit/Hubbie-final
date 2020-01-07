package com.example.hubbie.utilis.firestore

import android.util.Log
import com.example.hubbie.entities.Room
import com.example.hubbie.entities.User
import com.example.hubbie.utilis.ConvertDataUtils
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

object FirestoreRoomUtil {

    private const val COLLECTION_ROOM = "Room"

    const val ROOM_ADDED = com.example.hubbie.BuildConfig.APPLICATION_ID + ".ROOM_HAD_ADDED"
    const val ROOM_UPDATED = com.example.hubbie.BuildConfig.APPLICATION_ID + ".ROOM_HAD_UPDATED"
    const val ROOM_REMOVED = com.example.hubbie.BuildConfig.APPLICATION_ID + ".ROOM_HAD_REMOVED"

    private const val COLLECTION_ROOM_CONTROL = "RoomControl"
    private const val COLLECTION_USER = "User"
    private const val USER_NULL = com.example.hubbie.BuildConfig.APPLICATION_ID + ".USER_IS_NULL"
    private const val USER_DISABLE =
        com.example.hubbie.BuildConfig.APPLICATION_ID + ".USER_HAS_DISABLE"
    private val db = FirebaseFirestore.getInstance().collection(COLLECTION_ROOM)
    private val userRoom = FirebaseFirestore.getInstance().collection(COLLECTION_USER)

//    fun getBaseRoom(userId: String): Single<ArrayList<Room>> {
//        return Single.create {
//            userRoom.document(userId).collection(
//                COLLECTION_ROOM_CONTROL
//            ).get().addOnCompleteListener { task: Task<QuerySnapshot> ->
//                if (task.isSuccessful && task.result != null) {
//                    val roomList = ArrayList<Room>()
//                    if (roomList.size > 0) {
//                        roomList.clear()
//                    }
//                    val docs = task.result!!.documents
//                    for (item in docs) {
//                        val roomControl = item.toObject(RoomUser::class.java)
//                        if (roomControl != null) {
//                            if (roomControl.validate!!) {
//                                db.document(roomControl.id.toString()).get()
//                                    .addOnCompleteListener { task1 ->
//                                        if (task1.isSuccessful && task.result != null) {
//                                            val room = task1.result!!.toObject(Room::class.java)
//                                            if (room != null) {
//                                                roomList.add(room)
//                                                if (room.id == docs.last().id) {
//                                                    it.onSuccess(roomList)
//                                                }
//                                            }
//                                        }
//                                    }
//                            } else {
//                                if (item.id == docs.last().id) {
//                                    it.onSuccess(roomList)
//                                }
//                                it.onError(UserThrowable(USER_DISABLE))
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

    fun getBaseRoom(user: User): Single<ArrayList<Room>> {
        return Single.create {
            FirebaseFirestore.getInstance().collection(COLLECTION_USER).document(user.uid ?: "")
                .collection("DeviceList").get().addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        val result = ArrayList<Room>()
                        for (item in task.result!!.documents) {
                            Log.e("Test", "Key: $item")
                            db.document(item.id).get()
                                .addOnCompleteListener { task1: Task<DocumentSnapshot> ->
                                    if (task1.isSuccessful && task1.result != null) {
                                        result.add(ConvertDataUtils.convertDataToRoom(task1.result!!.data))
                                        if (result.last().id == item.id) {
                                            it.onSuccess(result)
                                        }
                                    }
                                }
                        }
                    }
                }
        }
    }

    var isFirstTime = false
    fun doRoomListener(userId: String): Observable<Pair<String, Room>> {
        return Observable.create {

            userRoom.document(userId).collection("DeviceList")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        return@addSnapshotListener
                    }

                    if (querySnapshot != null) {
                        if (isFirstTime) {
                            for (item in querySnapshot.documentChanges) {
                                var type = ""
                                when (item.type) {
                                    DocumentChange.Type.ADDED -> {
                                        type = ROOM_ADDED
                                    }
                                    DocumentChange.Type.MODIFIED -> {
                                        type = ROOM_ADDED
                                    }
                                    DocumentChange.Type.REMOVED -> {
                                        type = ROOM_ADDED
                                    }

                                }

                                val deviceSorted =
                                    ConvertDataUtils.convertDataToDeviceSort(item.document.data)
                                db.document(deviceSorted.macAddress).get()
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful && task.result != null) {
                                            val room =
                                                ConvertDataUtils.convertDataToRoom(task.result!!.data)
                                            it.onNext(Pair(type, room))
                                        }
                                    }

                            }

                        } else {
                            isFirstTime = true
                        }
                    }
                }

        }
    }

    fun getRoom(roomId: String): Single<Room> {
        return Single.create {
            db.document(roomId).get().addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    val room = task.result!!.toObject(Room::class.java)
                    if (room != null) {
                        it.onSuccess(room)
                    }
                }
            }
        }
    }

    fun deleteRoom(roomId: String) {
        db.document(roomId).delete()
    }

    fun setRoom(room: Room?): Completable {
        return Completable.create {
            if (room != null) {
                db.document(room.id ?: "").set(room)
            }
        }
    }

}