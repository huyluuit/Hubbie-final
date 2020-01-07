package com.example.hubbie.modules.main.view

import android.annotation.SuppressLint
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
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.hubbie.R
import com.example.hubbie.adapter.SectionsPagerAdapter
import com.example.hubbie.entities.*
import com.example.hubbie.entities.shared.AccountPreferences
import com.example.hubbie.modules.base.view.BaseFragment
import com.example.hubbie.modules.dialog.AddRoomFragmentDialog
import com.example.hubbie.modules.dialog.FragmentAddDeviceDialog
import com.example.hubbie.modules.main.IMain
import com.example.hubbie.modules.main.presenter.MainPresenter
import com.example.hubbie.utilis.GeneralUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : BaseFragment(), View.OnClickListener, ViewPager.OnPageChangeListener,
    DrawerLayout.DrawerListener, AddRoomFragmentDialog.EditRoomDialogCallbacks, IMain.View {
    override fun getBaseDeviceList(): ArrayList<DeviceSorted> {
        return baseDeviceSortedList
    }

    override fun getBaseUserList(): ArrayList<BaseUser> {
        return baseUserList
    }

    private lateinit var mDrawerLayout: DrawerLayout

    private lateinit var mNavigationView: NavigationView

    private lateinit var mBottomNavigationView: BottomNavigationView
    private var pageSelected = 0;
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
    private lateinit var tvUserNameDisplay: TextView
    private lateinit var prevMenu: MenuItem
    private var baseDeviceSortedList = ArrayList<DeviceSorted>()
    private var baseUserList = ArrayList<BaseUser>()

    private lateinit var mSectionsPagerAdapter: SectionsPagerAdapter
    private var isInitial = false

    private var isBaseUserHad = false
    private var isBaseDeviceHad = false
    private var presenter: MainPresenter? = null

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

    @SuppressLint("RestrictedApi")
    override fun onPageSelected(position: Int) {
        prevMenu.setChecked(false)
        mBottomNavigationView.menu.getItem(position).setChecked(true)
        tvTitle.setText(mBottomNavigationView.menu.getItem(position).title)
        prevMenu = mBottomNavigationView.menu.getItem(position)
        pageSelected = position;
        when (position) {
            0 -> {
                fabMenu.visibility = View.VISIBLE
                fabTvAdd.setText("TẠO PHÒNG")
            }
            1 -> {
                fabMenu.visibility = View.INVISIBLE
            }
            2 -> {
                fabMenu.visibility = View.VISIBLE
                fabTvAdd.setText("THÊM THIẾT BỊ")
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.e("HuyHUy", "HomeCLicking!")
        return when (item.itemId) {
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
        presenter = MainPresenter(this)
        val baseUser = AccountPreferences(context!!).getBaseAccount()
        presenter?.doUserChangeListener(baseUser.uid ?: "")
        presenter?.setView(this)
        LinearLayoutSetup(v)

        FABSetup(v) // Floating Action Bar setup

        ActionBarSetup(v)
        if ((baseUser.isActive ?: false)) {
            initialView(v, baseUser.role ?: "client")
            isInitial = true
        } else {
            showMessage(
                "THÔNG TIN TÀI KHOẢN",
                "TÀI KHOẢN CỦA BẠN ĐÃ BỊ KHÓA BỞI ADMIN! VUI LÒNG LIÊN HỆ ĐỂ BIẾT THÊM CHI TIẾT",
                "ĐĂNG XUẤT",
                "OK"
            )
        }

        return v
    }

    override fun accountActiveChangeState(result: Boolean) {
        Log.e("HuyHuy", "AccountStateChange: " + result)
        if (!result) {
            showMessage(
                "THÔNG TIN TÀI KHOẢN",
                "TÀI KHOẢN CỦA BẠN ĐÃ BỊ KHÓA BỞI ADMIN! VUI LÒNG LIÊN HỆ ĐỂ BIẾT THÊM CHI TIẾT",
                "ĐĂNG XUẤT",
                "OK"
            )
        } else {
            if (!isInitial) {
                if (view != null) {
                    initialView(
                        view!!,
                        AccountPreferences(context!!).getBaseAccount().role ?: "client"
                    )
                    isInitial = true
                }
            }
            dismissMessage()
        }
        hideView(result)

    }

    override fun onNegativeClick() {
        super.onNegativeClick()
        dismissMessage()
        presenter?.logOutClicked()
    }

    override fun onPositiveClick() {
        super.onPositiveClick()
        dismissMessage()
    }

    fun hideView(state: Boolean) {
        if (!state) {
            mViewPager.visibility = View.INVISIBLE
        } else {
            mViewPager.visibility = View.VISIBLE
        }
    }

    override fun onBaseUserList(baseUserList: ArrayList<BaseUser>) {
        this.baseUserList = baseUserList
        isBaseUserHad = true
        if (isBaseDeviceHad) {
            dismissLoading()
        }
        Log.e("HuyHuy", "BaseUser: $baseUserList")
    }

    override fun onBaseDeviceList(baseDeviceSortedList: ArrayList<DeviceSorted>) {
        this.baseDeviceSortedList = baseDeviceSortedList
        isBaseDeviceHad = true
        if (isBaseUserHad) {
            dismissLoading()
        }
        Log.e("HuyHuy", "BaseDevice: $baseDeviceSortedList")
    }

    @SuppressLint("RestrictedApi")
    private fun initialView(v: View, role: String) {

        SubsSetup(v) //Toolbar - NavigationLayout - Titile setup

        NavigationViewSetup(v)

        BottomNavigationViewSetup(v)

        //Check role
        when (role) {
            getString(R.string.admin) -> {
                fabMenu.visibility = View.VISIBLE
                showLoading()
                mSectionsPagerAdapter =
                    SectionsPagerAdapter(fragmentManager!!, 3, this)
            }

            getString(R.string.room_admin) -> {
                mBottomNavigationView.menu.removeItem(R.id.bottom_nav_device)
                mSectionsPagerAdapter =
                    SectionsPagerAdapter(fragmentManager!!, 2, this)
            }

            getString(R.string.client) -> {
                llAdd.visibility = View.INVISIBLE
                mBottomNavigationView.menu.removeItem(R.id.bottom_nav_device)
                mBottomNavigationView.menu.removeItem(R.id.bottom_nav_user)
                mSectionsPagerAdapter =
                    SectionsPagerAdapter(fragmentManager!!, 1, this)
            }
        }

        StructureSetup(v) //ViewPager - Tablayout setup
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
//        mActionBar = (activity as AppCompatActivity).supportActionBar!!
//        mActionBar.setHomeButtonEnabled(true)
//        mActionBar.setDisplayHomeAsUpEnabled(true)
//        mActionBar.setHomeAsUpIndicator(R.drawable.baseline_menu_24)
    }

    private fun SubsSetup(view: View) {
        tvTitle = mToolbar.findViewById(
            R.id.main_toolbar_title
        )
        mToolbar.ibMenu.setOnClickListener {
            mDrawerLayout.openDrawer(GravityCompat.START)
        }
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
        tvUserNameDisplay = mNavigationView.inflateHeaderView(R.layout.nav_header)
            .findViewById(R.id.tvUserNameDisplay)
        tvUserNameDisplay.setText(AccountPreferences(context!!).getBaseAccount().fullName)
        mNavigationView.setNavigationItemSelectedListener { p0: MenuItem ->
            // set item as selected to persist highlight
            p0.isChecked = true
            when (p0.itemId) {

                R.id.nav_my_info -> {
                    presenter?.navigationUserInfo()
                }

                R.id.nav_chat -> {
                    presenter?.navigationListChat()
                }

                R.id.nav_app_info -> {

                }

            }
            //set ActionBar Title
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

    override fun onRoomItemClicked(room: Room) {
        presenter?.onItemRoomClicked(room)
    }

    override fun onUserItemClicked(user: User) {
        presenter?.onItemUserClicked(user)
    }

    override fun onDeviceItemClicked(device: Device) {
        presenter?.onItemDeviceClicked(device)
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
            R.id.bottom_nav_device -> {
                mViewPager.setCurrentItem(2)
            }
            //R.id.bottom_nav_statistic -> { mViewPager.setCurrentItem(3) }
        }

    }

    override fun onDeleteRoom(room: Room) {

    }

    override fun onSaveRoom(room: Room) {
        Log.e("HuyHuy", "New Room: " + room)
        presenter?.onNewRoomCreated(room)
    }

    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.host_fab_menu -> {
                GeneralUtils.showHideLayout(llAdd)
                GeneralUtils.showHideLayout(llStatistic)
            }
            R.id.fab_add -> {
                //Run custom dialog create unit
                when (pageSelected) {
                    //Room
                    0 -> {
                        fragmentManager?.beginTransaction()?.add(
                            AddRoomFragmentDialog.newInstance(
                                this,
                                baseUserList,
                                baseDeviceSortedList
                            ) as Fragment,
                            AddRoomFragmentDialog::class.java.simpleName
                        )?.commitAllowingStateLoss()
                    }
                    //Device
                    2 -> {
                        fragmentManager?.beginTransaction()?.add(
                            FragmentAddDeviceDialog(AccountPreferences(context!!).getBaseAccount().uid!!) as Fragment,
                            FragmentAddDeviceDialog::class.java.simpleName
                        )?.commitAllowingStateLoss()
                    }
                }

            }
            R.id.fab_statistic -> {
                //Run custom dialog statistic
            }
        }

    }

}