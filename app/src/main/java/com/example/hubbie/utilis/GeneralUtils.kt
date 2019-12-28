package com.example.hubbie.utilis

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import android.view.View
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*


object GeneralUtils {

    fun isSDKAtLeastP(): Boolean {
        return Build.VERSION.SDK_INT >= 28
    }


    fun getSSID(context: Context?): Pair<Boolean,String>{
        var result = ""

        if(context != null){
            val wifiManager = (context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE) as WifiManager)
            val info = wifiManager.connectionInfo
            val disconnected = info == null || info.networkId == -1 || "<unknown ssid>" == info.ssid
            if(disconnected) {

            }
        }
            return Pair(false, result)
    }

    fun showHideLayout(view: View) {
        view.visibility = if (view.visibility == View.VISIBLE){
            View.INVISIBLE
        } else{
            View.VISIBLE
        }
    }

    fun showingToast(context: Context?, message: String?) {
        if (context != null && message != null && message.isNotEmpty()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun getTimeId(): String {
        val date = Calendar.getInstance().time
        val simpleDateFormat = SimpleDateFormat("yyyyMMddHHmmssSSSS", Locale.getDefault())
        val timeLong = java.lang.Long.parseLong(simpleDateFormat.format(date))
        return timeLong.toString()
    }
}