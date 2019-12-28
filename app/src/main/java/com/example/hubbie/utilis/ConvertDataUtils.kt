package com.example.hubbie.utilis

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

}