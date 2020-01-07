package com.example.hubbie.modules.device

import com.example.hubbie.entities.Device
import com.example.hubbie.entities.DeviceSorted
import com.example.hubbie.modules.base.BaseContract

interface IDevice {
    interface Presenter : BaseContract.BasePresenter {
        fun setView(view: View)
        fun getAllDevices()
        fun doDeviceChangeListener()
    }

    interface Interactor : BaseContract.BaseInteractor {
        fun getAllDevice()
        fun doDeviceChangeListener()
        interface Callbacks {
            fun onBaseDevice(deviceList: Pair<ArrayList<Device>, ArrayList<DeviceSorted>>)
            fun onNewDevice(device: Device)
            fun onUpdatedDevice(device: Device)
            fun onRemovedDevice(device: Device)
        }
    }

    interface View {
        fun setBaseDevice(deviceList: ArrayList<Device>)
        fun onNewDevie(position: Int)
        fun onUpdatedDevice(position: Int)
        fun onRemovedDevice(position: Int)
        fun onBaseDeviceSorted(deviceSorted: ArrayList<DeviceSorted>)
    }
}