package com.example.hubbie.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.hubbie.modules.room.view.RoomFragment

class SectionsPagerAdapter(fm: FragmentManager, pages: Int) : FragmentPagerAdapter(fm) {

    //Page of user. It depend on user permission
    val pages = pages
    private val TAG = "SectionsPagerAdapter"
    override fun getItem(p0: Int): Fragment {
        Log.e(TAG, "getItem number: " + p0)
        return when (p0) {
            0 -> RoomFragment()
            1 -> RoomFragment()
            2 -> RoomFragment()
            else -> RoomFragment()
        }
    }

    override fun getCount(): Int {
        return pages
    }

}