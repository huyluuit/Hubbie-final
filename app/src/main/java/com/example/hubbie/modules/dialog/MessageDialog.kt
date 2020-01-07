package com.example.hubbie.modules.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.hubbie.BuildConfig
import com.example.hubbie.R
import com.example.hubbie.modules.base.BaseContract
import com.example.hubbie.modules.base.view.BaseDialogFragment

class MessageDialogFragment( private val callbacks: BaseContract.MessageDialogCallbacks) : BaseDialogFragment(), View.OnClickListener {

    companion object {

        private const val ARG_TITLE = BuildConfig.APPLICATION_ID + ".args.TITLE"
        private const val ARG_MESSAGE = BuildConfig.APPLICATION_ID + ".args.MESSAGE"
        private const val ARG_NEGATIVE = BuildConfig.APPLICATION_ID + ".args.NEGATIVE"
        private const val ARG_NEUTRAL = BuildConfig.APPLICATION_ID + ".args.NEUTRAL"
        private const val ARG_POSITIVE = BuildConfig.APPLICATION_ID + ".args.POSITIVE"

        fun newInstance(title: String, message: String, neutral: String, callbacks: BaseContract.MessageDialogCallbacks
        ): MessageDialogFragment {
            val bundle = Bundle()
            bundle.putString(ARG_TITLE, title)
            bundle.putString(ARG_MESSAGE, message)
            bundle.putString(ARG_NEUTRAL, neutral)
            val dialog = MessageDialogFragment(callbacks)
            dialog.arguments = bundle
            return dialog
        }

        fun newInstance(
            title: String,
            message: String,
            negativeText: String,
            positiveText: String,
            callbacks: BaseContract.MessageDialogCallbacks
        ): MessageDialogFragment {
            val bundle = Bundle()
            bundle.putString(ARG_TITLE, title)
            bundle.putString(ARG_MESSAGE, message)
            bundle.putString(ARG_POSITIVE, positiveText)
            bundle.putString(ARG_NEGATIVE, negativeText)
            val dialog = MessageDialogFragment(callbacks)
            dialog.arguments = bundle
            return dialog
        }

    }

    private var title = ""
    private var message = ""
    private var btnNegativeText = ""
    private var btnPositiveText = ""
    private var btnNeutralText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments?.getString(ARG_TITLE, "").toString()
        message = arguments?.getString(ARG_MESSAGE, "").toString()
        btnNegativeText = arguments?.getString(ARG_NEGATIVE, "").toString()
        btnNeutralText = arguments?.getString(ARG_NEUTRAL, "").toString()
        btnPositiveText = arguments?.getString(ARG_POSITIVE, "").toString()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val v = activity?.layoutInflater?.inflate(R.layout.fragment_message_dialog, null)

        val tvTiltle: TextView? = v?.findViewById(R.id.tvMessageTitle)
        val tvMessage: TextView? = v?.findViewById(R.id.tvMessage)
        val btnNegative: Button? = v?.findViewById(R.id.btnNegative)
        val btnNeutral: Button? = v?.findViewById(R.id.btnNeutral)
        val btnPositive: Button? = v?.findViewById(R.id.btnPositive)

        tvTiltle?.text = title
        tvMessage?.text = message

        if (btnNeutralText.isEmpty()) {
            btnNeutral?.visibility = View.GONE
            btnNegative?.visibility = View.VISIBLE
            btnPositive?.visibility = View.VISIBLE
        } else {
            btnNeutral?.visibility = View.VISIBLE
            btnNegative?.visibility = View.GONE
            btnPositive?.visibility = View.GONE
        }

        if (btnNegativeText.isNotEmpty()) {
            btnNegative?.setText(btnNegativeText)
        }

        if (btnNeutralText.isNotEmpty()) {
            btnNeutral?.setText(btnNeutralText)
        }

        if (btnPositiveText.isNotEmpty()) {
            btnPositive?.setText(btnPositiveText)
        }

        btnPositive?.setOnClickListener(this)
        btnNegative?.setOnClickListener(this)
        btnNeutral?.setOnClickListener(this)

        val dialog = AlertDialog.Builder(activity).setView(v).create()
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return dialog
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnNegative -> {
                callbacks.onNegativeClick()
            }

            R.id.btnNeutral -> {
                callbacks.onNeutralClick()
            }

            R.id.btnPositive -> {
                callbacks.onPositiveClick()
            }
        }
    }

}