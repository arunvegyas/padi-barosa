package com.hiddencoders.cattleinsurance.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.annotation.StringRes
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.gson.JsonObject
import com.hiddencoders.cattleinsurance.R
import com.hiddencoders.cattleinsurance.data.model.LoginUserData
import com.hiddencoders.cattleinsurance.data.remote.ApiServices
import com.hiddencoders.cattleinsurance.databinding.ActivityLoginBinding
import com.hiddencoders.cattleinsurance.ui.BaseClass
import com.hiddencoders.cattleinsurance.ui.enrollment.EnrollmentActivity
import com.hiddencoders.cattleinsurance.ui.utilis.sharedPreferences.UserSession
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseClass() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btLogin: AppCompatButton

    @Inject
    lateinit var apiServices: ApiServices
    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialize()
    }

    private fun initialize() {
        etUsername = binding.username
        etPassword = binding.password
        btLogin = binding.login
        btLogin.setOnClickListener {
            if (isValidData()) {
                checkLogin(etUsername.text.toString(), etPassword.text.toString())
            }
        }
    }

    private fun checkLogin(username: String, password: String) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("username", username)
        jsonObject.addProperty("password", password)
        disposable = apiServices.verifyLogin(jsonObject).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.code == 0) {
                    userSession.setAccessToken(it.Data[0].ACCESSTOKEN)
                    userSession.setUsername(it.Data[0].username)
                    userSession.setUserId(it.Data[0].BCODE)
                    updateUiWithUser(it.Data[0])
                } else{
                    showToast(this,"Invalid login details")
                }
            }, {
                Log.d("TAG", "checkLogin: ")
            })
    }

    private fun isValidData(): Boolean {
        if (etUsername.text.isEmpty()) {
            etUsername.error = "Empty fields not allowed"
            return false
        } else if (etPassword.text.isEmpty()) {
            etPassword.error = "Empty fields not allowed"
            return false
        }
        return true
    }

    private fun updateUiWithUser(loginUserData: LoginUserData) {
        val welcome = getString(R.string.welcome)
        val displayName = loginUserData.username
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
        startActivity(Intent(this, EnrollmentActivity::class.java))
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}
