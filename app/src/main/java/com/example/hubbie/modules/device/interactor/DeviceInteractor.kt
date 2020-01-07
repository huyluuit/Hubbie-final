package com.example.hubbie.modules.device.interactor

import android.content.Context
import com.example.hubbie.entities.shared.AccountPreferences
import com.example.hubbie.modules.device.IDevice
import com.example.hubbie.utilis.firestore.FirestoreDeviceUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class DeviceInteractor(
    private val callbacks: IDevice.Interactor.Callbacks,
    private val context: Context
) :
    IDevice.Interactor {

    private val compositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        compositeDisposable.dispose()
    }

    override fun doDeviceChangeListener() {
        val baseUser = AccountPreferences(context).getBaseAccount()
        val deviceListener = FirestoreDeviceUtil.doEventChangeListener(baseUser.uid ?: "")
            .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    when (it.first) {
                        FirestoreDeviceUtil.DEVICE_ADDED -> callbacks.onNewDevice(it.second)
                        FirestoreDeviceUtil.DEVICE_UPDATED -> callbacks.onUpdatedDevice(it.second)
                        FirestoreDeviceUtil.DEVICE_REMOVED -> callbacks.onRemovedDevice(it.second)
                    }
                },
                onError = {

                }
            )
    }

    override fun getAllDevice() {
        val baseUser = AccountPreferences(context).getBaseAccount()
        val deviceDisposable =
            FirestoreDeviceUtil.getAllDeviceList(baseUser.uid ?: "").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeBy(
                    onSuccess = {
                        callbacks.onBaseDevice(it)
                    },
                    onError = {

                    }
                )
        compositeDisposable.add(deviceDisposable)
    }

}