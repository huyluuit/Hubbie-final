package com.example.hubbie.utilis.firestore

import android.util.Log
import com.example.hubbie.entities.User
import com.example.hubbie.utilis.ConvertDataUtils
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

object FirestoreUserUtil {

    const val USER_ADDED = com.example.hubbie.BuildConfig.APPLICATION_ID + ".USER_HAD_ADDED"
    const val USER_UPDATED = com.example.hubbie.BuildConfig.APPLICATION_ID + ".USER_HAD_UPDATED"
    const val USER_REMOVED = com.example.hubbie.BuildConfig.APPLICATION_ID + ".USER_HAD_REMOVED"
    const val COLLECTION_USER = "User"
    private val db = FirebaseFirestore.getInstance().collection(COLLECTION_USER)

    /* User DB Structure
     User(C) > User_Id(D) > RoomControl(C) > Room_Id(D) > RoomUser_Object: roomId: String?
                                                                           activate: Boolean?
     */

    fun getUser(userId: String): Single<User> {
        return Single.create {
            db.document(userId).get().addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    Log.e("User", "NameDisplay: ${task.result?.data?.toString()}")
                    val user = ConvertDataUtils.convertDataToUser(task.result!!.data)
                    it.onSuccess(user)
                }
            }
        }
    }

    private var firstRun = false
    fun doUserListChangeListener(userId: String): Observable<Pair<String, User>> {
        return Observable.create {
            db.addSnapshotListener { querySnapshot, firebaseFirestoreException ->

                if (firebaseFirestoreException != null) {
                    return@addSnapshotListener
                }

                if (querySnapshot != null) {
                    if (firstRun) {
                        for (item in querySnapshot.documentChanges) {
                            val user = ConvertDataUtils.convertDataToUser(item.document.data)
                            Log.e("HuyHuy", "UserListChange: " + user)
                            if (user.adminId == userId) {
                                when (item.type) {
                                    DocumentChange.Type.ADDED -> {
                                        it.onNext(Pair(USER_ADDED, user))
                                    }

                                    DocumentChange.Type.MODIFIED -> {
                                        it.onNext(Pair(USER_UPDATED, user))
                                    }

                                    DocumentChange.Type.REMOVED -> {
                                        it.onNext(Pair(USER_REMOVED, user))
                                    }
                                }
                            }
                        }
                    } else {
                        firstRun = true
                    }
                }

            }
        }
    }

    fun doUserChangeListener(userId: String): Observable<Pair<String, User>> {
        return Observable.create {
            db.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    return@addSnapshotListener
                }
                if (documentSnapshot != null) {
                    for (item in documentSnapshot.documentChanges) {
                        if (item == DocumentChange.Type.REMOVED && item.document.id == userId) {
                            val result = Pair(
                                USER_REMOVED,
                                ConvertDataUtils.convertDataToUser(item.document.data)
                            )
                            it.onNext(result)
                            break
                        }
                    }
                }
            }

            db.document(userId)
                .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        return@addSnapshotListener
                    }
                    if (documentSnapshot != null) {
                        val result = Pair(
                            USER_UPDATED,
                            ConvertDataUtils.convertDataToUser(documentSnapshot.data)
                        )
                        it.onNext(result)
                    }
                }
        }
    }

    fun getBaseUserList(user: User): Single<ArrayList<User>> {
        return Single.create {
            val userDb = FirebaseFirestore.getInstance().collection(COLLECTION_USER).get()
            userDb.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    val result = ArrayList<User>()
                    for (item in task.result!!.documents) {
                        val userData = ConvertDataUtils.convertDataToUser(item.data)
                        if (userData.adminId == user.uid) {
                            result.add(userData)
                        }
                    }
                    it.onSuccess(result)
                }
            }
        }
    }

    fun setUser(user: User?): Completable {
        return Completable.create {
            if (user != null) {
                db.document(user.uid!!).set(user).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        it.onComplete()
                    } else {
                        return@addOnCompleteListener
                    }
                }
            }
        }
    }

    fun checkAdminId(userId: String): Single<Boolean> {
        return Single.create {
            db.document(userId).get().addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    //user exist!
                    val role = task.result!!.data?.get("role").toString()
                    if (role == "admin" || role == "roomAdmin") {
                        it.onSuccess(true)
                    }
                } else {
                    it.onSuccess(false)
                }
            }
        }
    }

    fun removeUser(userId: String) {
        db.document(userId).delete().addOnCompleteListener { task: Task<Void> ->
            if (task.isSuccessful) {

            } else {
                return@addOnCompleteListener
            }
        }
    }

    fun setUserState(userId: String, state: Boolean) {
        db.document(userId).update("active", state)
    }

}