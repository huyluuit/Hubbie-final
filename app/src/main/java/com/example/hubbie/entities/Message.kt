package com.example.hubbie.entities

class Message {
    var message : String? = null
    var time : String? = ""
    var uid : String? = null
        get() = field
        set(value) {
            field = value
        }
}