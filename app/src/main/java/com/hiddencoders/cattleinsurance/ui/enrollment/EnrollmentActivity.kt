package com.hiddencoders.cattleinsurance.ui.enrollment

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import com.hiddencoders.cattleinsurance.R
import com.hiddencoders.cattleinsurance.databinding.ActivityEnrollmentBinding
import com.hiddencoders.cattleinsurance.ui.farmers.FarmersActivity
import com.hiddencoders.cattleinsurance.ui.claims.ClaimsActivity
import com.hiddencoders.cattleinsurance.ui.tags.TagsActivity
import com.hiddencoders.cattleinsurance.ui.utilis.sharedPreferences.UserSession
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EnrollmentActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {
    private lateinit var binding: ActivityEnrollmentBinding
    private lateinit var popupMenu: PopupMenu

    @Inject
    lateinit var userSession: UserSession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnrollmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setClickListners()
    }

    private fun setClickListners() {
        binding.username.text = "Hi ${userSession.getUsername()}, ${userSession.getUserId()}"
        popupMenu = PopupMenu(this, binding.popupMenu)
        popupMenu.setOnMenuItemClickListener(this)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.popup_menu, popupMenu.menu)
        binding.popupMenu.setOnClickListener {
            popupMenu.show()
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_tagging -> {
                startActivity(Intent(this, TagsActivity::class.java))
            }
            R.id.item_retagging -> {
                startActivity(Intent(this, ClaimsActivity::class.java))
            }
            R.id.item_farmers -> {
                startActivity(Intent(this, FarmersActivity::class.java))
            }
            R.id.item_claim ->{
                startActivity(Intent(this,ClaimsActivity::class.java))
            }
        }
        return true
    }
}