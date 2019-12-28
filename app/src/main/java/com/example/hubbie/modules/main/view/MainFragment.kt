package com.example.hubbie.modules.main.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.example.hubbie.R
import com.example.hubbie.adapter.SectionsPagerAdapter
import com.example.hubbie.entities.shared.AccountPreferences
import com.example.hubbie.modules.base.view.BaseFragment
import com.example.hubbie.utilis.GeneralUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout

class MainFragment : BaseFragment(), View.OnClickListener, ViewPager.OnPageChangeListener,
    DrawerLayout.DrawerListener {

    private lateinit var mDrawerLayout: DrawerLayout

    private lateinit var mNavigationView: NavigationView

    private lateinit var mBottomNavigationView: BottomNavigationView
    private lateinit var mToolbar: Toolbar

    private lateinit var mActionBar: ActionBar

    private lateinit var tvTitle: TextView

    private lateinit var mViewPager: ViewPager

    private lateinit var llAdd: LinearLayout
    private lateinit var llStatistic: LinearLayout

    private lateinit var fabMenu: FloatingActionButton
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var fabStatistic: FloatingActionButton
    private lateinit var fabTvAdd: TextView

    private lateinit var prevMenu: MenuItem

    private lateinit var  mSectionsPagerAdapter: SectionsPagerAdapter

    override fun onDrawerStateChanged(newState: Int) {
    }

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
    }

    override fun onDrawerClosed(drawerView: View) {
    }

    override fun onDrawerOpened(drawerView: View) {
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        prevMenu.setChecked(false)
        mBottomNavigationView.menu.getItem(position).setChecked(true)
        tvTitle.setText(mBottomNavigationView.menu.getItem(position).title)
        prevMenu = mBottomNavigationView.menu.getItem(position)

        when (position){
            0 -> fabTvAdd.setText("TẠO PHÒNG")
            1 -> fabTvAdd.setText("TẠO NGƯỜI DÙNG")
            2 -> fabTvAdd.setText("THÊM THIẾT BỊ")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                mDrawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_main, container, false)

        val baseUser = AccountPreferences(context!!).getBaseAccount()

        Log.e("User", "User nameDisplay: ${baseUser.fullName} Role: ${baseUser.role}")

        when(baseUser.role){
            getString(R.string.admin) -> {
                mSectionsPagerAdapter =
                    SectionsPagerAdapter(fragmentManager!!, 3)
            }

            getString(R.string.room_admin) -> {
                mSectionsPagerAdapter =
                    SectionsPagerAdapter(fragmentManager!!, 2)
            }

            getString(R.string.client) -> {
                mSectionsPagerAdapter =
                    SectionsPagerAdapter(fragmentManager!!, 1)
            }
            else -> {
                mSectionsPagerAdapter =
                    SectionsPagerAdapter(fragmentManager!!, 1)
            }
        }

        LinearLayoutSetup(v)

        FABSetup(v) // Floating Action Bar setup

        ActionBarSetup(v)

        StructureSetup(v) //ViewPager - Tablayout setup

        SubsSetup(v) //Toolbar - NavigationLayout - Titile setup

        NavigationViewSetup(v)

        BottomNavigationViewSetup(v)
        return v
    }

    //Component setup functions: This is first setup at onCreate function.
    private fun LinearLayoutSetup(view: View) {
        llAdd = view.findViewById(R.id.ll_add)
        llAdd.visibility = View.INVISIBLE
        llStatistic = view.findViewById(R.id.ll_statistic)
        llStatistic.visibility = View.INVISIBLE
    }

    private fun FABSetup(view: View) {
        fabMenu = view.findViewById(R.id.host_fab_menu)
        fabAdd = view.findViewById(R.id.fab_add)
        fabTvAdd = view.findViewById(R.id.fab_tv_add)
        fabTvAdd.setText("TẠO PHÒNG")
        fabStatistic = view.findViewById(R.id.fab_statistic)
        //fabAdd.visibility = View.INVISIBLE
        //fabStatistic.visibility =  View.INVISIBLE
        fabMenu.setOnClickListener(this)
        fabAdd.setOnClickListener(this)
        fabStatistic.setOnClickListener(this)
    }

    private fun ActionBarSetup(view: View) {
        mToolbar = view.findViewById(R.id.main_toolbar)
        (activity as AppCompatActivity).setSupportActionBar(mToolbar)
        mActionBar = (activity as AppCompatActivity).supportActionBar!!
        mActionBar.setDisplayHomeAsUpEnabled(true)
        mActionBar.setHomeAsUpIndicator(R.drawable.baseline_menu_24)
    }

    private fun SubsSetup(view: View) {
        tvTitle = mToolbar.findViewById(
            R.id.main_toolbar_title
        )
        mDrawerLayout = view.findViewById(R.id.main_drawer_layout)
        mDrawerLayout.addDrawerListener(this)
    }

    private fun StructureSetup(view: View) {

        mViewPager = view.findViewById(R.id.vp_container)
        mViewPager.adapter = mSectionsPagerAdapter!!
        mViewPager.offscreenPageLimit = 3
        mViewPager.addOnPageChangeListener(this)
        //mTabLayout = findViewById(R.id.tab_layout)
        //mTabLayout.setupWithViewPager(mViewPager!!)
    }

    private fun NavigationViewSetup(view: View) {
        mNavigationView = view.findViewById(R.id.main_nav_view)
        mNavigationView.setNavigationItemSelectedListener { p0: MenuItem ->
            // set item as selected to persist highlight
            p0.isChecked = true

            //set ActionBar Title
            tvTitle.setText(p0.title)
            // close drawer when item is tapped
            mDrawerLayout.closeDrawers()
            //calling the method displayselectedscreen and passing the id of selected menu
            displaySelectedScreen(p0.itemId)
            true
        }
    }

    private fun BottomNavigationViewSetup(view: View) {
        mBottomNavigationView = view.findViewById(R.id.bottom_nav_view)
        tvTitle.setText("Rooms")
        prevMenu = mBottomNavigationView.menu.getItem(0)
        mBottomNavigationView.setOnNavigationItemSelectedListener { p0: MenuItem ->
            p0.isChecked = true
            tvTitle.setText(p0.title)
            displaySelectedScreen(p0.itemId)
            true
        }
    }

    private fun displaySelectedScreen(itemId: Int) {

        when (itemId) {
            R.id.nav_my_info -> {
            }
            R.id.bottom_nav_room -> {
                mViewPager.setCurrentItem(0)
            }
            R.id.bottom_nav_user -> {
                mViewPager.setCurrentItem(1)
            }
            R.id.bottom_nav_hw -> {
                mViewPager.setCurrentItem(2)
            }
            //R.id.bottom_nav_statistic -> { mViewPager.setCurrentItem(3) }
        }

    }

    override fun onClick(v: View?) {

        when (v!!.id){
            R.id.host_fab_menu -> {
                GeneralUtils.showHideLayout(llAdd)
                GeneralUtils.showHideLayout(llStatistic)
            }
            R.id.fab_add -> {
                //Run custom dialog create unit
            }
            R.id.fab_statistic -> {
                //Run custom dialog statistic
            }
        }

    }

}