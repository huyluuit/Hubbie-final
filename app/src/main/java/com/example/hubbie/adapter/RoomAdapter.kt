package com.example.hubbie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hubbie.R
import com.example.hubbie.entities.Room

class RoomAdapter(private val data: ArrayList<Room>, private val listener: Callbacks) :
    RecyclerView.Adapter<RoomAdapter.DataViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_room,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {

        holder.tvRoomName.text = data[position].nameDisplay

        holder.swLoad1.setOnCheckedChangeListener { buttonView, isChecked ->
            holder.bindSw1Click(position, isChecked, listener)
        }
        holder.swLoad2.setOnCheckedChangeListener { buttonView, isChecked ->
            holder.bindSw2Click(position, isChecked, listener)
        }
        holder.swLoad3.setOnCheckedChangeListener { buttonView, isChecked ->
            holder.bindSw1Click(position, isChecked, listener)
        }

        holder.itemView.setOnClickListener {
            holder.bindItemClick(position, listener)
        }

    }

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvRoomName: TextView
        val tvTotalPower: TextView
        val tvLoadname1: TextView
        val tvLoadname2: TextView
        val tvLoadname3: TextView
        val swLoad1: Switch
        val swLoad2: Switch
        val swLoad3: Switch
        val llLoad1: LinearLayout
        val llLoad2: LinearLayout
        val llLoad3: LinearLayout

        init {
            tvRoomName = itemView.findViewById(R.id.tvRoomName)
            tvTotalPower = itemView.findViewById(R.id.tvTotalPower)
            llLoad1 = itemView.findViewById(R.id.llLoad1Frame)
            llLoad2 = itemView.findViewById(R.id.llLoad2Frame)
            llLoad3 = itemView.findViewById(R.id.llLoad3Frame)
            tvLoadname1 = itemView.findViewById(R.id.tvLoadName1)
            tvLoadname2 = itemView.findViewById(R.id.tvLoadName2)
            tvLoadname3 = itemView.findViewById(R.id.tvLoadName3)
            swLoad1 = itemView.findViewById(R.id.swLoad1)
            swLoad2 = itemView.findViewById(R.id.swLoad2)
            swLoad3 = itemView.findViewById(R.id.swLoad3)
        }

        fun bindItemClick(position: Int, listener: Callbacks) {
            listener.onItemClick(position)
        }

        fun bindSw1Click(position: Int, result: Boolean, listener: Callbacks) {
            listener.onSw1Click(position, result)
        }

        fun bindSw2Click(position: Int, result: Boolean, listener: Callbacks) {
            listener.onSw2Click(position, result)
        }

        fun bindSw3Click(position: Int, result: Boolean, listener: Callbacks) {
            listener.onSw3Click(position, result)
        }
    }

    interface Callbacks {
        fun onItemClick(position: Int)
        fun onSw1Click(position: Int, result: Boolean)
        fun onSw2Click(position: Int, result: Boolean)
        fun onSw3Click(position: Int, result: Boolean)
    }

}