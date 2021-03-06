package com.example.hubbie.utilis

import com.example.hubbie.entities.*

object ConvertDataUtils {


    fun convertDataToUser(data: Map<String, Any>?): User {

        var user = User()

        if (data != null) {
            val isActive: Boolean? = data["active"] as Boolean
            val uid: String? = data["uid"].toString()
            val email: String? = data["email"].toString()
            val phone: String? = data["phone"].toString()
            val fullName: String? = data["fullName"].toString()
            val pwd: String? = data["pwd"].toString()
            val role: String? = data["role"].toString()
            val roomId: String? = data["roomId"].toString()
            val adminId: String? = data["adminId"].toString()

            if (isActive != null && uid != null && email != null && phone != null && fullName != null && pwd != null && role != null && adminId != null) {
                user = User(isActive, uid, email, phone, fullName, pwd, role, adminId, roomId)
            }
        }

        return user

    }

    fun convertDataToMessage(data: Map<String, Any>?): Message {
        val message = Message()
        if (data != null) {
            val content: String? = data["message"].toString()
            val time: String? = data["time"].toString()
            val uid: String? = data["uid"].toString()
            message.message = content
            message.time = time
            message.uid = uid
        }
        return message
    }

    fun convertDataToDeviceSort(data: Map<String, Any>?): DeviceSorted {

        val deviceSorted = DeviceSorted()

        if (data != null) {
            val macAddress: String = data["macAddress"].toString()
            val ipAddress: String = data["ipAddress"].toString()
            val roomName: String = data["roomName"].toString()
            deviceSorted.macAddress = macAddress
            deviceSorted.ipAddress = ipAddress
            deviceSorted.roomName = roomName
        }

        return deviceSorted
    }

    fun convertDataToRoom(data: Map<String, Any>?): Room {
        val room = Room()

        if (data != null) {
            val id: String? = data["id"].toString();
            val userAdmin: String? = data["userAdmin"].toString()
            val nameDisplay: String? = data["nameDisplay"].toString()
            val ipAddress: String? = data["ipAddress"].toString()
            val role: Boolean? = data["role"] as Boolean
            room.id = id
            room.userAdmin = userAdmin
            room.nameDisplay = nameDisplay
            room.ipAddress = ipAddress
            room.role = role
        }
        return room
    }

    fun convertDataToDevice(data: Map<String, Any>?): Device {

        val device = Device()

        if (data != null) {
            val id: String? = data["id"].toString()
            val ip: String? = data["ip"].toString()
            val isMaster: Boolean? = data["master"] as Boolean
            val portAState: Boolean? = data["portAState"] as Boolean
            val portBState: Boolean? = data["portBState"] as Boolean
            val portCState: Boolean? = data["portCState"] as Boolean
            val powerA: Float? = data["powerA"] as Float
            val powerB: Float? = data["powerB"] as Float
            val powerC: Float? = data["powerC"] as Float
            val totalPower: Float? = data["totalPower"] as Float

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