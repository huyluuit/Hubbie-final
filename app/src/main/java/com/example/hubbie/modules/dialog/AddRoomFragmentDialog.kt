package com.example.hubbie.modules.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hubbie.BuildConfig
import com.example.hubbie.R
import com.example.hubbie.adapter.PortIdAdapter
import com.example.hubbie.entities.BaseUser
import com.example.hubbie.entities.DeviceSorted
import com.example.hubbie.entities.Room
import com.example.hubbie.entities.shared.AccountPreferences
import com.example.hubbie.modules.base.view.BaseDialogFragment
import com.example.hubbie.utilis.GeneralUtils

class AddRoomFragmentDialog(
    private val callbacks: EditRoomDialogCallbacks,
    private val baseUserList: ArrayList<BaseUser>,
    private val baseDevieList: ArrayList<DeviceSorted>
) : BaseDialogFragment(), PortIdAdapter.Callbacks {

    companion object {

        private const val ARGS_ROOM = BuildConfig.APPLICATION_ID + ".args.ROOM"

        fun newInstance(
            room: Room,
            callbacks: EditRoomDialogCallbacks,
            baseUserList: ArrayList<BaseUser>,
            baseDevieList: ArrayList<DeviceSorted>
        ): AddRoomFragmentDialog {

            val dialog = AddRoomFragmentDialog(callbacks, baseUserList, baseDevieList)
            val bundle = Bundle()
            bundle.putParcelable(ARGS_ROOM, room)
            dialog.arguments = bundle
            return dialog

        }

        fun newInstance(
            callbacks: EditRoomDialogCallbacks, baseUserList: ArrayList<BaseUser>,
            baseDevieList: ArrayList<DeviceSorted>
        ): AddRoomFragmentDialog {
            val dialog = AddRoomFragmentDialog(callbacks, baseUserList, baseDevieList)
            return dialog
        }

    }

    private lateinit var etDeviceId: EditText
    private lateinit var etRoomName: EditText

    private lateinit var btnDeleteRoom: Button
    private lateinit var btnCancel: Button
    private lateinit var btnSaveChanges: Button

    private lateinit var tvNotify: TextView

    private lateinit var rg: RadioGroup

    private lateinit var rvRoomList: RecyclerView

    private lateinit var autoCompleteTextView: AutoCompleteTextView

    private var positionClick = 0

    private var simpleAdapter = PortIdAdapter(baseDevieList, this);

    fun convertUserData(): ArrayList<String> {
        val result = ArrayList<String>()
        for (item in baseUserList) {
            result.add(item.nameDisplay ?: "")
        }
        return result
    }

    override fun onItemClick(position: Int) {
        etDeviceId.setText(baseDevieList[position].macAddress)
        etRoomName.setText(baseDevieList[position].roomName)
        positionClick = position
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val v = activity?.layoutInflater?.inflate(R.layout.fragment_add_room_dialog, null)
        val room: Room? = arguments?.getParcelable(ARGS_ROOM)

        autoCompleteTextView = v?.findViewById(R.id.autoTvUserList)!!
        val adapter =
            ArrayAdapter(v.context, android.R.layout.select_dialog_item, convertUserData())
        autoCompleteTextView.threshold = 0
        autoCompleteTextView.setAdapter(adapter)


        tvNotify = v.findViewById(R.id.tvNotify)

        etDeviceId = v.findViewById(R.id.etDeviceId)!!
        etRoomName = v.findViewById(R.id.etRoomNameDisplay)

        btnDeleteRoom = v.findViewById(R.id.btnDelete)
        btnDeleteRoom.setOnClickListener {
            if (room != null) {
                callbacks.onDeleteRoom(room)
            }
        }
        btnCancel = v.findViewById(R.id.btnCancel)
        btnCancel.setOnClickListener {
            dismiss()
        }
        btnSaveChanges = v.findViewById(R.id.btnSave)
        btnSaveChanges.setOnClickListener {

            val nameDisplay = etRoomName.text.toString()
            val deviceId = etDeviceId.text.toString()

            if (validateUserInput(nameDisplay, deviceId)) {
                if (rg.checkedRadioButtonId == R.id.rdCommon) {
                    callbacks.onSaveRoom(
                        Room(
                            deviceId,
                            AccountPreferences(context!!).getBaseAccount().uid,
                            nameDisplay,
                            baseDevieList[positionClick].ipAddress,
                            true
                        )
                    )
                    dismiss()
                } else if (rg.checkedRadioButtonId == R.id.rdPrivate) {
                    if (autoCompleteTextView.text.isNotEmpty()) {
                        val userNameDisplay = autoCompleteTextView.text.toString()
                        var p = 0
                        for (item in baseUserList) {
                            if (item.nameDisplay == userNameDisplay) {
                                break
                            }
                            p++
                        }
                        callbacks.onSaveRoom(
                            Room(
                                deviceId,
                                baseUserList[p].uid,
                                nameDisplay,
                                baseDevieList[positionClick].ipAddress,
                                false
                            )
                        )
                        dismiss()
                    } else {
                        GeneralUtils.showingToast(context, "Phòng riêng chưa có người quản lý!")
                    }
                }
            }

        }

        rg = v.findViewById(R.id.rgRoomRole)
        rg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rdCommon -> {
                    autoCompleteTextView.isEnabled = false
                }
                R.id.rdPrivate -> {
                    autoCompleteTextView.isEnabled = true
                }
            }
        }

        rvRoomList = v.findViewById(R.id.rvDeviceList)
        rvRoomList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        rvRoomList.adapter = simpleAdapter
        rvRoomList.setHasFixedSize(true)

        when (room) {
            //create new
            null -> {
                btnDeleteRoom.visibility = View.GONE
                rvRoomList.visibility = View.VISIBLE
            }
            // edit room
            else -> {
                tvNotify.visibility = View.GONE
                rvRoomList.visibility = View.GONE

                etDeviceId.setText(room.id)
                etRoomName.setText(room.nameDisplay)

                when (room.role) {
                    // private
                    false -> {
                        rg.check(R.id.rdPrivate)
                    }

                    true -> {
                        rg.check(R.id.rdCommon)
                    }
                }

            }
        }

        val dialog = AlertDialog.Builder(activity).setView(v).create()
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        return dialog
    }

    private fun validateUserInput(nameDisplay: String, deviceId: String): Boolean {

        if (nameDisplay.isEmpty()) {
            GeneralUtils.showingToast(context, "Tên phòng còn trống!")
            return false
        }

        if (deviceId.isEmpty()) {
            GeneralUtils.showingToast(context, "Mã thiết bị còn trống!")
            return false
        }

        return true
    }

    interface EditRoomDialogCallbacks {
        fun onDeleteRoom(room: Room)
        fun onSaveRoom(room: Room)
    }

}