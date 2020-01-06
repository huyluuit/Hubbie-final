package com.example.hubbie.modules.main.interactor

import android.content.Context
import android.util.Log
import com.example.hubbie.entities.Room
import com.example.hubbie.entities.shared.AccountPreferences
import com.example.hubbie.modules.main.IMain
import com.example.hubbie.utilis.firestore.FirestoreUserUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers


class MainInteractor(
    private val context: Context?,
    private val callbacks: IMain.Interactor.Callbacks
) : IMain.Interactor {

    private val compositeDisposable = CompositeDisposable()

    override fun addNewRoom(room: Room) {

    }

    override fun doAccountChangeListener(userId: String) {
        val userDisposable =
            FirestoreUserUtil.doUserChangeListener(userId).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    when (it.first) {

                        FirestoreUserUtil.USER_UPDATED -> {
                            if (context != null) {
                                val lastState =
                                    AccountPreferences(context).getBaseAccount().isActive
                                AccountPreferences(context).setBaseAccount(it.second)
                                if (lastState != it.second.isActive) {
                                    Log.e("HuyHUy", "AccountChange: " + it.second.isActive)
                                    callbacks.onAccountActivateChanges(it.second.isActive ?: false)
                                }
                            }
                        }

                        FirestoreUserUtil.USER_REMOVED -> {
                            if (context != null) {
                                AccountPreferences(context).removeBaseAccount()
                                callbacks.onAccountDelete()
                            }
                        }
                    }
                }

        compositeDisposable.add(userDisposable)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }

}
