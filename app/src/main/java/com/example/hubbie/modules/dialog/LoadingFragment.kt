
package com.example.hubbie.modules.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.hubbie.R
import com.example.hubbie.modules.base.view.BaseDialogFragment

class LoadingFragment : BaseDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val v = activity?.layoutInflater?.inflate(R.layout.fragment_loading, null)

        isCancelable = false

        val dialog = AlertDialog.Builder(activity)
            .setView(v)
            .create()

        dialog.window?.apply {
            // set background transparent
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            // set full screen
        }

        return dialog
    }
}