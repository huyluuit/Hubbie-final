package com.example.hubbie.modules.main.presenter

import com.example.hubbie.entities.Device
import com.example.hubbie.entities.Room
import com.example.hubbie.entities.User
import com.example.hubbie.entities.shared.AccountPreferences
import com.example.hubbie.modules.main.IMain
import com.example.hubbie.modules.main.IMain.*
import com.example.hubbie.modules.main.interactor.MainInteractor
import com.example.hubbie.modules.main.route.MainRoute
import com.example.hubbie.modules.main.view.MainFragment

class MainPresenter(private val fragment: MainFragment) : Presenter, Interactor.Callbacks {

    private var interactor: MainInteractor? = MainInteractor(fragment.context, this)
    private var router: MainRoute? = MainRoute(fragment)
    private var view: View? = null

    override fun setView(view: IMain.View) {
        this.view = view
    }

    override fun onAccountDelete() {
        router?.navigateToLogin()
    }

    override fun onAccountActivateChanges(state: Boolean) {
        view?.accountActiveChangeState(state)
    }

    override fun logOutClicked() {
        router?.navigateToLogin()
        AccountPreferences(fragment.context!!).removeBaseAccount()
    }

    override fun doUserChangeListener(userId: String) {
        interactor?.doAccountChangeListener(userId)
    }

    override fun onMyInfoClicked() {

    }

    override fun onChatClick() {

    }

    override fun createRoomClicked() {

    }

    override fun onNewRoomCreated(room: Room) {

    }

    override fun onItemRoomClicked(room: Room) {

    }

    override fun onItemUserClicked(user: User) {

    }

    override fun onItemDeviceClicked(device: Device) {

    }

    fun navigationUserInfo() {
        router?.navigationUserInfor()
    }

    fun navigationListChat() {
        router?.navigationListChat()
    }

    override fun onDestroy() {
        router = null
        interactor = null
    }


}
