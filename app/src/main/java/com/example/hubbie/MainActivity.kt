package com.example.hubbie

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.hubbie.entities.shared.AccountPreferences
import com.example.hubbie.modules.login.view.LoginFragment
import com.example.hubbie.modules.main.view.MainFragment

class MainActivity : AppCompatActivity() {

    private lateinit var frameLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        frameLayout = findViewById(R.id.main_container_frame)
        selectFragment()
    }

    private fun selectFragment() {
        val checkAccountPreferences = AccountPreferences(this)
        val transaction = supportFragmentManager.beginTransaction()

        if (checkAccountPreferences.getAccountExist()) {
            //run to room fragment
            transaction.replace(frameLayout.id, MainFragment()).commit()
        } else {
            //go to login fragment
            transaction.replace(frameLayout.id, LoginFragment()).commit()
        }
    }
}
