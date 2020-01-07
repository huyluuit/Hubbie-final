package com.example.hubbie.modules.device.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hubbie.R
import com.example.hubbie.adapter.DeviceAdapter
import com.example.hubbie.entities.Device
import com.example.hubbie.entities.DeviceSorted
import com.example.hubbie.modules.base.view.BaseFragment
import com.example.hubbie.modules.device.IDevice
import com.example.hubbie.modules.device.presenter.DevicePresenter
import com.example.hubbie.modules.main.IMain

class DeviceFragment(private val listener: IMain.View) : BaseFragment(), IDevice.View,
    DeviceAdapter.OnItemClick {
    override fun onBaseDeviceSorted(deviceSorted: ArrayList<DeviceSorted>) {
        listener.onBaseDeviceList(deviceSorted)
        this.deviceSorted = deviceSorted
        if(isDeviceExist){
            deviceAdapter = DeviceAdapter(deviceList, deviceSorted, this)
            rvDevice.adapter = deviceAdapter
            rvDevice.setHasFixedSize(true)
            presenter.doDeviceChangeListener()
        }else{
            isDeviceSortedExist = true
        }

    }

    private lateinit var presenter: DevicePresenter
    private lateinit var rvDevice: RecyclerView
    private var deviceList = ArrayList<Device>()
    private var deviceSorted = ArrayList<DeviceSorted>()
    private lateinit var deviceAdapter: DeviceAdapter
    private var isDeviceSortedExist = false
    private var isDeviceExist = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_device, container, false)
        presenter = DevicePresenter(this)
        presenter.setView(this)
        presenter.getAllDevices()
        rvDevice = v.findViewById(R.id.rvDevice)
        rvDevice.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        return v
    }

    override fun setBaseDevice(deviceList: ArrayList<Device>) {
        this.deviceList = deviceList
        if(isDeviceSortedExist){
            deviceAdapter = DeviceAdapter(deviceList, deviceSorted, this)
            rvDevice.adapter = deviceAdapter
            rvDevice.setHasFixedSize(true)
            presenter.doDeviceChangeListener()
        }else{
            isDeviceExist = true
        }
    }

    override fun onItemClick(position: Int) {

    }

    override fun onNewDevie(position: Int) {
        deviceAdapter.notifyItemInserted(position)
    }

    override fun onUpdatedDevice(position: Int) {
        deviceAdapter.notifyItemChanged(position)
    }

    override fun onRemovedDevice(position: Int) {
        deviceAdapter.notifyItemRemoved(position)
    }

}