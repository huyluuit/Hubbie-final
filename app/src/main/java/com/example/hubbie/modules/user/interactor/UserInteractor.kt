package com.example.hubbie.modules.user.interactor

import android.content.Context
import android.util.Log
import com.example.hubbie.entities.User
import com.example.hubbie.modules.user.IUser
import com.example.hubbie.utilis.firestore.FirestoreUserUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class UserInteractor(private val listener: IUser.Interactor.Callbacks) :
    IUser.Interactor {

    private val compositeDisposable = CompositeDisposable()

    override fun getAllUsers(user: User) {
        val userDisposable = FirestoreUserUtil.getBaseUserList(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribeBy(
                onSuccess = {
                    listener.onBaseUser(it)
                    Log.e("HuyHuy", "UserList: " + it)
                },
                onError = {

                }
            )
        compositeDisposable.add(userDisposable)
    }

    override fun doUserListChange(userId: String) {
        val userDisposable =
            FirestoreUserUtil.doUserListChangeListener(userId).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribeBy(
                    onNext = {
                        when (it.first) {
                            FirestoreUserUtil.USER_ADDED -> listener.onAddUserItem(it.second)
                            FirestoreUserUtil.USER_UPDATED -> listener.onUpdateUserItem(it.second)
                            FirestoreUserUtil.USER_REMOVED -> listener.onUserDelete(it.second)
                        }
                    },
                    onError = {

                    }
                )
        compositeDisposable.add(userDisposable)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }


}