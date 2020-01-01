package com.example.hubbie.modules.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.hubbie.BuildConfig
import com.example.hubbie.R
import com.example.hubbie.entities.Room
import com.example.hubbie.modules.base.view.BaseDialogFragment

class AddRoomFragmentDialog : BaseDialogFragment() {

    companion object{

        private const val ARGS_ROOM = BuildConfig.APPLICATION_ID + ".args.ROOM"

        fun newInstance(room: Room): AddRoomFragmentDialog{

            val dialog = AddRoomFragmentDialog()
            val bundle = Bundle()
            bundle.putParcelable(ARGS_ROOM, room)
            dialog.arguments = bundle
            return dialog

        }

        fun newInstance(): AddRoomFragmentDialog{
            val dialog = AddRoomFragmentDialog()
            return dialog
        }

    }

    private lateinit var etDeviceId: EditText
    private lateinit var etRoomName: EditText

    private lateinit var btnUpdate: Button
    private lateinit var btnDeleteRoom: Button
    private lateinit var btnCancel: Button
    private lateinit var btnSaveChanges: Button

    private lateinit var tvNotify: TextView

    private lateinit var rg: RadioGroup

    private lateinit var rvRoomList: RecyclerView

    private lateinit var autoCompleteTextView: AutoCompleteTextView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val v = activity?.layoutInflater?.inflate(R.layout.fragment_add_room_dialog, null)
        val room: Room? = arguments?.getParcelable(ARGS_ROOM)

        autoCompleteTextView = v?.findViewById(R.id.autoTvUserList)!!

        tvNotify = v.findViewById(R.id.tvNotify)

        etDeviceId = v.findViewById(R.id.etDeviceId)!!
        etRoomName = v.findViewById(R.id.etRoomNameDisplay)

        btnUpdate = v.findViewById(R.id.btnDeviceUpdate)
        btnDeleteRoom = v.findViewById(R.id.btnDelete)
        btnCancel = v.findViewById(R.id.btnCancel)
        btnSaveChanges = v.findViewById(R.id.btnSave)

        rg = v.findViewById(R.id.rgRoomRole)

        rvRoomList = v.findViewById(R.id.rvDeviceList)

        when(room){
            //create new
            null -> {

            }
            // edit room
            else -> {
                tvNotify.visibility = View.GONE
                btnUpdate.visibility = View.GONE
                rvRoomList.visibility = View.GONE

                etDeviceId.setText(room.deviceId)
                etRoomName.setText(room.nameDisplay)

                when(room.role){
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

}