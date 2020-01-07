package com.example.hubbie.modules.user.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hubbie.R
import com.example.hubbie.adapter.UserAdapter
import com.example.hubbie.entities.BaseUser
import com.example.hubbie.entities.User
import com.example.hubbie.modules.base.view.BaseFragment
import com.example.hubbie.modules.dialog.FragmentUserDetailDialog
import com.example.hubbie.modules.main.IMain
import com.example.hubbie.modules.user.IUser
import com.example.hubbie.modules.user.presenter.UserPresenter

class UserFragment(private val listener: IMain.View) : BaseFragment(), UserAdapter.Callbacks,
    IUser.View {

    private var userList = ArrayList<User>()
    private var presenter: UserPresenter? = null
    private lateinit var userAdapter: UserAdapter
    private lateinit var rvUserList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_user, container, false)
        presenter = UserPresenter(this)
        presenter?.setView(this)
        presenter?.getAllUsers()
        rvUserList = v.findViewById(R.id.rvUser)
        rvUserList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        return v
    }

    override fun onRoleClick(position: Int) {
        showMessage("Thông tin người dùng", "Vai trò: " + userList[position].role, "OK")
    }

    override fun onBaseUserList(baseUserList: ArrayList<BaseUser>) {
        listener.onBaseUserList(baseUserList)
    }

    override fun onUserInfoClick(position: Int) {
        val dialog = FragmentUserDetailDialog(userList[position])
        fragmentManager?.beginTransaction()
            ?.add(dialog as Fragment, FragmentUserDetailDialog::class.java.simpleName)
            ?.commitAllowingStateLoss()
    }

    override fun onLogClick(position: Int) {
        listener.onUserItemClicked(userList[position])
    }

    override fun onNeutralClick() {
        dismissMessage()
    }

    override fun setBaseUser(userList: ArrayList<User>) {
        this.userList = userList
        userAdapter = UserAdapter(this.userList, this)
        rvUserList.adapter = userAdapter
        rvUserList.setHasFixedSize(true)
        presenter?.doUserListChangeListener()
    }

    override fun onNewUserAdd(position: Int) {
        userAdapter.notifyItemInserted(position)
    }

    override fun onUserUpdate(position: Int) {
        userAdapter.notifyItemChanged(position)
    }

    override fun onUserDelete(position: Int) {
        userAdapter.notifyItemRemoved(position)
    }

}
