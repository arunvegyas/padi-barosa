package com.hiddencoders.cattleinsurance.ui.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.hiddencoders.cattleinsurance.databinding.ActivitySplashScreenBinding
import com.hiddencoders.cattleinsurance.ui.enrollment.EnrollmentActivity
import com.hiddencoders.cattleinsurance.ui.utilis.sharedPreferences.UserSession
import com.hiddencoders.cattleinsurance.ui.verify.VerifyMobileNumberActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {
    @Inject
    lateinit var userSession: UserSession
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Handler().postDelayed(Runnable {
            if (userSession.getAccessToken().isNotEmpty()){
                startActivity(Intent(this,EnrollmentActivity::class.java))
            } else{
                startActivity(Intent(this,VerifyMobileNumberActivity::class.java))
            }
            finish()
        },3000)
    }
}