package com.hiddencoders.cattleinsurance.ui.farmers

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hiddencoders.cattleinsurance.data.model.Centers
import com.hiddencoders.cattleinsurance.data.model.FarmersData
import com.hiddencoders.cattleinsurance.databinding.ActivityFarmersBinding
import com.hiddencoders.cattleinsurance.ui.BaseClass
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FarmersActivity : BaseClass() {
    private lateinit var binding: ActivityFarmersBinding
    private var getCenterList: ArrayList<Centers> = ArrayList()
    private val viewmodel by viewModels<FarmersViewmodel>()
    private lateinit var farmerAdapter: FarmersAdapter
    private var farmerList: ArrayList<FarmersData> = ArrayList()
    private var ccode: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFarmersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getCentersData(0)
        setData()
    }

    private fun setData() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        farmerAdapter = FarmersAdapter(this, farmerList)
        binding.rvTags.layoutManager = LinearLayoutManager(this)
        binding.rvTags.setHasFixedSize(true)
        binding.rvTags.adapter = farmerAdapter
        Handler(Looper.getMainLooper()).post {
            viewmodel.getCenterFarmerList(0, "")
                .observeForever {
                    if (it.code == "0") {
                        farmerList.clear()
                        farmerList.addAll(it.Farmers)
                        farmerAdapter.notifyDataSetChanged()
                    }
                }
        }
        binding.svFarmer.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun filter(newText: String) {
        val filteredlist: ArrayList<FarmersData> = ArrayList()
        filteredlist.clear()
        // running a for loop to compare elements.
        for (item in farmerList) {

            val first_name = item.FARMERNAME

            if (first_name.toLowerCase().contains(newText!!.toLowerCase())
            ) {
                filteredlist.add(item)
            }
        }
        farmerAdapter.filterList(filteredlist)
    }

    private fun getCentersData(code: Int) {
        Handler(Looper.getMainLooper()).post {
            viewmodel.getCenters(code).observeForever {
                if (it.code == "0") {
                    val list = arrayOfNulls<String>(it.Centers.size)
                    getCenterList.clear()
                    getCenterList.addAll(it.Centers)
                    for (i in 0 until it.Centers.size) {
                        list[i] = it.Centers[i].CNAME
                    }

                    val adapter = ArrayAdapter(
                        this,
                        android.R.layout.simple_spinner_item, list
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spCenterName.adapter = adapter

                    binding.spCenterName.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                val mandalName = parent?.getItemAtPosition(position).toString()
                                ccode = getCenterList[position].CCODE
                                Handler(Looper.getMainLooper()).post {
                                    viewmodel.getCenterFarmerList(ccode, "")
                                        .observeForever {
                                            if (it.code == "0") {
                                                farmerList.clear()
                                                farmerList.addAll(it.Farmers)
                                                farmerAdapter.filterList(farmerList)
                                            }
                                        }
                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }

                        }
                }
            }
        }
    }
}