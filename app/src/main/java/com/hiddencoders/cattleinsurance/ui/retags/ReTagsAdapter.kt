package com.hiddencoders.cattleinsurance.ui.retags

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hiddencoders.cattleinsurance.R
import com.hiddencoders.cattleinsurance.data.model.Tags
import com.hiddencoders.cattleinsurance.ui.claims.ClaimsAdapter
import com.hiddencoders.cattleinsurance.ui.utilis.convertDateFormat

class ReTagsAdapter(val mContext: Context, val list: ArrayList<Tags>, val listner: onUpdateListener) :
    RecyclerView.Adapter<ReTagsAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name = view.findViewById<TextView>(R.id.tvFarmerName)
        var date = view.findViewById<TextView>(R.id.tvDate)
        var type = view.findViewById<TextView>(R.id.tvBuffaleType)
        var tagNo = view.findViewById<TextView>(R.id.tvTagNo)
        var rctNo = view.findViewById<TextView>(R.id.tvRctNo)
        var isApproved = view.findViewById<TextView>(R.id.tvApproved)
        var createdBy = view.findViewById<TextView>(R.id.tvCreatedBy)
        var breed = view.findViewById<TextView>(R.id.tvBuffaleBreed)
        var animalStatus = view.findViewById<TextView>(R.id.tvAnimalStatus)
        var update = view.findViewById<ImageView>(R.id.tvEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root = LayoutInflater.from(mContext).inflate(R.layout.tag_cell, parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.name.text = data.FARMERNAME
        holder.date.text = convertDateFormat("yyyy-MM-dd'T'HH:mm:ss", "dd MMM, yyyy", data.TDATE)
        holder.tagNo.text = "Tag No : ${data.TAGNO}"
        holder.rctNo.text = "Receipt No : ${data.RCTNO}"
        holder.createdBy.text = "Created by : ${data.CREATEDBY}"
        if (data.ANIMAL_STATUS==1) {
            holder.animalStatus.text = "Status : Milking"
        } else {
            holder.animalStatus.text = "Status : Pregnent"
        }
        if (data.BREED == "1"){
            holder.breed.text = "Breed : Local"
        } else{
            holder.breed.text = "Breed : Cross Breed"
        }
        if (data.ISAPPROVED) {
            holder.isApproved.text = "Approved"
            holder.update.visibility = View.GONE
        } else {
            holder.isApproved.text = "Pending"
            holder.update.visibility = View.GONE
        }

        holder.update.setOnClickListener {
            listner.updateTag(data.CATID.toString())
        }
    }

    override fun getItemCount(): Int = list.size
    interface onUpdateListener{
        fun updateTag(id:String)
    }
}