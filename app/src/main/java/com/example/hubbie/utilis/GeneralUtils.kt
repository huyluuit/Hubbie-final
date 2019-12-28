package com.example.hubbie.utilis

import android.content.Context
import android.view.View
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*


object GeneralUtils {


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