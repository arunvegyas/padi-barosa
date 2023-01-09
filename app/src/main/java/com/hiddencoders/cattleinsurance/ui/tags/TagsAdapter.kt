package com.hiddencoders.cattleinsurance.ui.tags

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hiddencoders.cattleinsurance.R
import com.hiddencoders.cattleinsurance.data.model.Tags
import com.hiddencoders.cattleinsurance.ui.utilis.convertDateFormat

class TagsAdapter(val mContext: Context, val list: ArrayList<Tags>) :
    RecyclerView.Adapter<TagsAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
         var name = view.findViewById<TextView>(R.id.tvFarmerName)
         var date = view.findViewById<TextView>(R.id.tvDate)
         var type = view.findViewById<TextView>(R.id.tvBuffaleType)
         var tagNo = view.findViewById<TextView>(R.id.tvTagNo)
         var rctNo = view.findViewById<TextView>(R.id.tvRctNo)
         var isApproved = view.findViewById<TextView>(R.id.tvApproved)
         var createdBy = view.findViewById<TextView>(R.id.tvCreatedBy)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root = LayoutInflater.from(mContext).inflate(R.layout.tag_cell,parent,false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.name.text = data.FARMERNAME
        holder.date.text = convertDateFormat("yyyy-MM-dd'T'HH:mm:ss", "dd MMM, yyyy",data.TDATE)
        holder.tagNo.text = "Tag No : ${data.TAGNO}"
        holder.rctNo.text = "Receipt No : ${data.RCTNO}"
        holder.createdBy.text = "Created by : ${data.CREATEDBY}"
        if (data.ISAPPROVED){
            holder.isApproved.text = "Approved"
        } else{
            holder.isApproved.text = "Pending"
        }
    }

    override fun getItemCount(): Int = list.size
}