package com.example.hubbie.entities

import android.os.Parcel
import android.os.Parcelable

data class Room(
    var id: String? = "",
    var nameDisplay: String? = "",
    var deviceId: String? = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(nameDisplay)
        parcel.writeString(deviceId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Room> {
        override fun createFromParcel(parcel: Parcel): Room {
            return Room(parcel)
        }

        override fun newArray(size: Int): Array<Room?> {
            return arrayOfNulls(size)
        }
    }
}
//Client Control Map Structure: ClientId, Validate?

data class RoomUser(
    val id: String? = "",
    val userId: String? = "",
    var validate: Boolean? = false
)