package com.example.hubbie.modules.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.hubbie.R
import com.example.hubbie.modules.base.view.BaseDialogFragment
import com.example.hubbie.utilis.GeneralUtils
import java.lang.ref.WeakReference
import java.util.ArrayList

class FragmentAddDeviceDialog : BaseDialogFragment() {

    companion object{
        private const val REQUEST_PERMISSION = 0x01
    }

    private lateinit var etSsid: EditText
    private lateinit var etPwd: EditText
    private lateinit var btnStartSmartConfig: Button

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val v = activity?.layoutInflater?.inflate(R.layout.fragment_add_device_dialog, null)

        if (v != null) {
            etSsid = v.findViewById(R.id.etSsid)
            etPwd = v.findViewById(R.id.etPwd)
        }

        val dialog = AlertDialog.Builder(activity).setView(v).create()
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        requestLocationPermission()

        return dialog

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    interface DialogBtnClick{
        fun onSmartConfigClick()
        fun onOkClick()
    }

    fun requestLocationPermission(){
        if (this.context!!.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
            ActivityCompat.requestPermissions(this.activity!!,permissions, REQUEST_PERMISSION)
        } else {
            val ssid = GeneralUtils.getSSID(context)
            btnStartSmartConfig.isEnabled = true
            if(ssid.first){
                etSsid.setText(ssid.second)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == REQUEST_PERMISSION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                btnStartSmartConfig.isEnabled = true
                requestLocationPermission()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}