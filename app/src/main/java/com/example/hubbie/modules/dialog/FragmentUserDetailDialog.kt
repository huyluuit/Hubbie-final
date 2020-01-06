package com.example.hubbie.modules.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.hubbie.R
import com.example.hubbie.entities.User
import com.example.hubbie.modules.base.view.BaseDialogFragment
import com.example.hubbie.utilis.firestore.FirestoreUserUtil

class FragmentUserDetailDialog(private val user: User) : BaseDialogFragment() {

    private var userState = false;

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val v = activity?.layoutInflater?.inflate(R.layout.fragment_user_detail, null)

        val dialog = AlertDialog.Builder(activity).setView(v).create()
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val tvUserNameDisplay: TextView? = v?.findViewById(R.id.tvUserNameDisplay)
        val tvEmail: TextView? = v?.findViewById(R.id.tvEmail)
        val tvPhone: TextView? = v?.findViewById(R.id.tvPhone)
        val tvRole: TextView? = v?.findViewById(R.id.tvRole)
        val tvState: TextView? = v?.findViewById(R.id.tvState)
        val btnDisable: Button? = v?.findViewById(R.id.btnDisable)
        btnDisable?.setOnClickListener {
            FirestoreUserUtil.setUserState(user.uid ?: "", !userState)
            showMessage(
                "Thông báo",
                "BẠN VỪA ${btnDisable.text} TÀI KHOẢN: ${user.fullName}",
                "Ok"
            )
        }

        val btnDelete: Button? = v?.findViewById(R.id.btnRemoveAccount)
        btnDelete?.setOnClickListener {
            showMessage(
                "Thông báo",
                "Bạn xóa tài khoản: ${user.fullName}",
                "Hủy",
                "Đồng Ý"
            )
        }

        tvUserNameDisplay?.text = user.fullName
        tvPhone?.text = user.phone
        tvRole?.text = user.role
        if ((user.isActive ?: false)) {
            userState = true
            tvState?.text = "ACTIVATE"
            btnDisable?.setText("VÔ HIỆU HÓA")
        } else {
            userState = false
            btnDisable?.setText("KÍCH HOẠT")
            tvState?.text = "DE-ACTIVATE"
        }
        return dialog
    }

    override fun onNegativeClick() {
        super.onNegativeClick()
        dismissMessage()
    }

    override fun onNeutralClick() {
        super.onNeutralClick()
        dismissMessage()
        dismiss()
    }

    override fun onPositiveClick() {
        super.onPositiveClick()
        FirestoreUserUtil.removeUser(user.uid?:"")
        dismissMessage()
        dismiss()
    }
}