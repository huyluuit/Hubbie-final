package com.example.hubbie.modules.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hubbie.R
import com.example.hubbie.modules.base.view.BaseDialogFragment

class AddRoomFragmentDialog : BaseDialogFragment() {

    private lateinit var rvDeviceList: RecyclerView
    private lateinit var etDeviceId: EditText
    private lateinit var etRoomName: EditText

    private lateinit var btnUpdateRoomName: Button
    private lateinit var btnDeleteRoom: Button
    private lateinit var btnCancel: Button
    private lateinit var btnSaveChange: Button

    private lateinit var rg: RadioGroup

    companion object{

        fun newInstance(): AddRoomFragmentDialog{

            val dialog = AddRoomFragmentDialog()

            return dialog

        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val v = activity?.layoutInflater?.inflate(R.layout.fragment_add_room_dialog, null)

        val dialog = AlertDialog.Builder(activity).setView(v).create()
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        return dialog

    }

}