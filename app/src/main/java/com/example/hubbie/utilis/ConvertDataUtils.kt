package com.example.hubbie.utilis

import com.example.hubbie.entities.Device
import com.example.hubbie.entities.DeviceSorted
import com.example.hubbie.entities.User

object ConvertDataUtils {


    fun convertDataToUser(data: Map<String, Any>?): User {

        var user = User()

        if (data != null) {
            val isActive: Boolean? = data["active"] as Boolean
            val uid: String? = data["uid"] as String
            val email: String? = data["email"] as String
            val phone: String? = data["phone"] as String
            val fullName: String? = data["fullName"] as String
            val pwd: String? = data["pwd"] as String
            val role: String? = data["role"] as String

            if (isActive != null && uid != null && email != null && phone != null && fullName != null && pwd != null && role != null) {
                user = User(isActive, uid, email, phone, fullName, pwd, role)
            }
        }

        return user

    }

    fun convertDataToDeviceSort(data: Map<String, Any>?): DeviceSorted {

        val deviceSorted = DeviceSorted()

        if (data != null) {
            val macAddress: String = data["macAddress"].toString()
            val ipAddress: String = data["ipAddress"].toString()
            deviceSorted.macAddress = macAddress
            deviceSorted.ipAddress = ipAddress
        }

        return deviceSorted

    }

    fun convertDataToDevice(data: Map<String, Any>?): Device {

        val device = Device()

        if (data != null) {
            val id: String? = data["id"].toString()
            var ip: String? = data["ip"].toString()
            var isMaster: Boolean? = data["master"] as Boolean
            var portAState: Boolean? = data["portAState"] as Boolean
            var portBState: Boolean? = data["portBState"] as Boolean
            var portCState: Boolean? = data["portCState"] as Boolean
            var powerA: Float? = data["powerA"] as Float
            var powerB: Float? = data["powerB"] as Float
            var powerC: Float? = data["powerC"] as Float
            var totalPower: Float? = data["totalPower"] as Float

            device.id = id
            device.ip = ip
            device.isMaster = isMaster
            device.portAState = portAState
            device.portBState = portBState
            device.portCState = portCState
            device.powerA = powerA
            device.powerB = powerB
            device.powerC = powerC
            device.totalPower = totalPower

        }

        return device

    }

}