package com.hiddencoders.cattleinsurance.ui.claims

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hiddencoders.cattleinsurance.data.model.ClaimTags
import com.hiddencoders.cattleinsurance.data.model.Tags
import com.hiddencoders.cattleinsurance.databinding.ActivityTagsBinding
import com.hiddencoders.cattleinsurance.ui.BaseClass
import com.hiddencoders.cattleinsurance.ui.claim.ClaimActivity
import com.hiddencoders.cattleinsurance.ui.retags.ClaimsViewmodel
import com.hiddencoders.cattleinsurance.ui.tagging.EditTaggingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClaimsActivity : BaseClass(), ClaimsAdapter.onUpdateListener {
    private lateinit var binding: ActivityTagsBinding
    private val viewmodel by viewModels<ClaimsViewmodel>()
    private var tagsList: ArrayList<ClaimTags> = ArrayList()
    private lateinit var adapter: ClaimsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTagsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setData()
    }

    override fun onResume() {
        super.onResume()
        setData()
    }

    private fun setData() {
        binding.tvTitle.text = "Claim"
        adapter = ClaimsAdapter(this, tagsList,this)
        binding.rvTags.layoutManager = LinearLayoutManager(this)
        binding.rvTags.setHasFixedSize(true)
        binding.rvTags.adapter = adapter
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.fab.setOnClickListener {
            startActivity(Intent(this, ClaimActivity::class.java))
        }
        Handler(Looper.getMainLooper()).post {
            viewmodel.getReTags().observeForever {
                if (it.code == "0") {
                    tagsList.clear()
                    tagsList.addAll(it.Tags)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun updateTag(id: String) {
        startActivity(Intent(this, EditTaggingActivity::class.java).putExtra("id",id))
    }
}