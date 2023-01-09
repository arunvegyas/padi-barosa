package com.hiddencoders.cattleinsurance.ui.farmers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hiddencoders.cattleinsurance.R
import com.hiddencoders.cattleinsurance.data.model.FarmersData

class FarmersAdapter(val mContext: Context, var list: ArrayList<FarmersData>) :
    RecyclerView.Adapter<FarmersAdapter.Viewholder>() {
    class Viewholder(view: View) : RecyclerView.ViewHolder(view) {
        var name = view.findViewById<TextView>(R.id.tvFarmerName)
        var tvMobile = view.findViewById<TextView>(R.id.tvMobile)
        var tvFather = view.findViewById<TextView>(R.id.tvfather)
        var tvUID = view.findViewById<TextView>(R.id.tvUID)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val root = LayoutInflater.from(mContext).inflate(R.layout.farmer_cell, parent, false)
        return Viewholder(root)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val data = list[position]
        holder.name.text = data.FARMERNAME
        holder.tvMobile.text = data.CONTACTNO
        holder.tvFather.text = "S/o,D/o,W/o : ${data.FATHERNAME}"
        holder.tvUID.text = "UID : ${data.UID}"
    }

    fun filterList(filtered: ArrayList<FarmersData>){
        list = filtered
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = list.size
}