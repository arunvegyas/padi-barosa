package com.hiddencoders.cattleinsurance.ui.retagging

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hiddencoders.cattleinsurance.databinding.ActivityRetaggingBinding

class RetaggingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRetaggingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRetaggingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setData()
    }

    private fun setData() {
        
    }
}