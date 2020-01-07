package com.example.hubbie.modules.electricStatistic.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.hubbie.R
import com.example.hubbie.entities.User
import com.example.hubbie.modules.electricStatistic.IElectricStatistic
import com.example.hubbie.modules.electricStatistic.presenter.ElectricStatisticPresenter
import com.example.hubbie.modules.message.listChat.view.ListChatFragment
import kotlinx.android.synthetic.main.fragment_electricity_statistic.view.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ElectricStatisticFragment : Fragment(), IElectricStatistic.View {

    private var getView : View? = null
    private var user : User? = null
    private var presenter : ElectricStatisticPresenter? = null
    private var electricityConsumption : Double? = null

    companion object{

        fun newInstance(user: User) : ElectricStatisticFragment{
            val fragment = ElectricStatisticFragment()
            val bundle = Bundle()
            bundle.putParcelable(ListChatFragment.ARG_USER, user)
            fragment.arguments = bundle
            return fragment
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        presenter = ElectricStatisticPresenter()
        getView = inflater.inflate(R.layout.fragment_electricity_statistic, container, false)
        getBundleData()
        getElecttricityConsumption()
        initView()
        calculateElectricityBill()
        return getView
    }

    private fun initView(){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        getView?.txt_name_value?.text = user?.fullName
        getView?.txt_role_value?.text = user?.role
        getView?.txt_room_value?.text = user?.roomId
        getView?.txt_date?.text = "$year/$month/$day"
    }

    private fun getBundleData(){
        user = arguments?.getParcelable(ListChatFragment.ARG_USER)
    }

    private fun calculateElectricityBill() : Double{
        val bill = electricityConsumption?.times(2500.0) ?: 0.0
        getView?.txt_electricity_bill?.text = "$bill  VND"
        return bill
    }

    private fun getElecttricityConsumption(){
        electricityConsumption = 24.6
        getView?.txt_electricity_consumption?.text = "$electricityConsumption  kW/h"
        //Call getDataElectricity to assign value for electricity consumption variable
//        presenter?.getDataElectricity()
    }

}
