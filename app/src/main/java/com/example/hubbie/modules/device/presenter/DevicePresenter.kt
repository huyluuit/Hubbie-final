package com.example.hubbie.modules.device.presenter

import com.example.hubbie.entities.Device
import com.example.hubbie.entities.DeviceSorted
import com.example.hubbie.modules.device.IDevice
import com.example.hubbie.modules.device.interactor.DeviceInteractor
import com.example.hubbie.modules.device.view.DeviceFragment

class DevicePresenter(private val fragment: DeviceFragment) : IDevice.Presenter,
    IDevice.Interactor.Callbacks {


    private val deviceList = ArrayList<Device>()
    private var interactor: DeviceInteractor? = DeviceInteractor(this, fragment.context!!)
    private var view: IDevice.View? = null

    override fun setView(view: IDevice.View) {
        this.view = view
    }

    override fun getAllDevices() {
        interactor?.getAllDevice()
    }

    override fun doDeviceChangeListener() {
        interactor?.doDeviceChangeListener()
    }

    override fun onDestroy() {
        interactor = null
    }

    override fun onBaseDevice(deviceList: Pair<ArrayList<Device>, ArrayList<DeviceSorted>>) {
        this.deviceList.addAll(deviceList.first)
        view?.setBaseDevice(this.deviceList)
        view?.onBaseDeviceSorted(deviceList.second)
    }

    override fun onNewDevice(device: Device) {
        this.deviceList.add(device)
        view?.onNewDevie(this.deviceList.lastIndex)
    }

    override fun onUpdatedDevice(device: Device) {
        var p = 0
        for (item in deviceList) {
            if (device.id == item.id) {
                break
            }
            p++
        }
        deviceList.set(p, device)
        view?.onUpdatedDevice(p)
    }

    override fun onRemovedDevice(device: Device) {
        var p = 0
        for (item in deviceList) {
            if (device.id == item.id) {
                break
            }
            p++
        }
        deviceList.remove(device)
        view?.onRemovedDevice(p)
    }

}