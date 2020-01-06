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
import com.example.hubbie.modules.base.view.BaseFragment
import com.example.hubbie.modules.device.IDevice
import com.example.hubbie.modules.device.presenter.DevicePresenter
import com.example.hubbie.modules.main.IMain

class DeviceFragment(private val listener: IMain.View) : BaseFragment(), IDevice.View,
    DeviceAdapter.OnItemClick {

    private lateinit var presenter: DevicePresenter
    private lateinit var rvDevice: RecyclerView
    private var deviceList = ArrayList<Device>()
    private lateinit var deviceAdapter: DeviceAdapter

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
        deviceAdapter = DeviceAdapter(deviceList, this)
        rvDevice.adapter = deviceAdapter
        rvDevice.setHasFixedSize(true)
        presenter.doDeviceChangeListener()
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