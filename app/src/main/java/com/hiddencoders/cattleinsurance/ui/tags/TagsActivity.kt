package com.hiddencoders.cattleinsurance.ui.tags

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hiddencoders.cattleinsurance.data.model.Tags
import com.hiddencoders.cattleinsurance.databinding.ActivityTagsBinding
import com.hiddencoders.cattleinsurance.ui.BaseClass
import com.hiddencoders.cattleinsurance.ui.tagging.TaggingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TagsActivity : BaseClass() {
    private lateinit var binding: ActivityTagsBinding
    private val viewmodel by viewModels<TagsViewmodel>()
    private var tagsList: ArrayList<Tags> = ArrayList()
    private lateinit var adapter: TagsAdapter
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
        adapter = TagsAdapter(this, tagsList)
        binding.rvTags.layoutManager = LinearLayoutManager(this)
        binding.rvTags.setHasFixedSize(true)
        binding.rvTags.adapter = adapter
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.fab.setOnClickListener {
            startActivity(Intent(this,TaggingActivity::class.java))
        }
        Handler(Looper.getMainLooper()).post {
            viewmodel.getTags().observeForever {
                if (it.code == "0") {
                    tagsList.clear()
                    tagsList.addAll(it.Tags)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
}