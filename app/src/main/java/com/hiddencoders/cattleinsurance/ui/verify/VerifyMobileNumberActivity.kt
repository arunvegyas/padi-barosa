package com.hiddencoders.cattleinsurance.ui.verify

import `in`.aabhasjindal.otptextview.OTPListener
import `in`.aabhasjindal.otptextview.OtpTextView
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hiddencoders.cattleinsurance.R
import com.hiddencoders.cattleinsurance.data.remote.ApiServices
import com.hiddencoders.cattleinsurance.databinding.ActivityVerifyMobileNumberBinding
import com.hiddencoders.cattleinsurance.helper.SmsBroadcastReceiver
import com.hiddencoders.cattleinsurance.ui.BaseClass
import com.hiddencoders.cattleinsurance.ui.utilis.RESOLVE_HINT
import com.hiddencoders.cattleinsurance.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@AndroidEntryPoint
class VerifyMobileNumberActivity : BaseClass() {
    private lateinit var binding: ActivityVerifyMobileNumberBinding

    /*Retrieve OTP*/
    private val REQ_USER_CONSENT: Int = 200
    lateinit var smsBroadcastReceiver: SmsBroadcastReceiver
    var message: String = ""
    private lateinit var pinview_OTP: OtpTextView
    private lateinit var disposable: Disposable
    private var sOtp:String=""

    @Inject
    lateinit var apiServices: ApiServices

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyMobileNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setonClickListner()
        getPhoneNumberByTelephony()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setonClickListner() {
        binding.btSendOtp.setOnClickListener {
            if (binding.etMobileNumber.text.toString().isNotEmpty()) {
                sendOTP(binding.etMobileNumber.text.toString())
            }
        }
    }

    private fun sendOTP(mobile: String) {
        val url = "http://43.242.125.150/padibharosa/verifyuser/$mobile"
        disposable = apiServices.verifyUserByMobileNumber(url).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                if (it.code == "0") {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        openVerifyOTPBottomsheet(it.Data[0].otp)
                    }
                } else {
                    showToast(this, it.description)
                }
            }, {
                Log.d("TAG", "sendOTP: ")
            })
    }

    private fun getPhoneNumberByTelephony() {
        try {
            val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val tel = tm.line1Number
            if (tel != null) {
                Log.d("TAG", "MobileNumber By Telephony: $tel")
                setMobileNumber(tel)
            }

        } catch (e: SecurityException) {
            Log.d("TAG", "MobileNumber By Telephony: ${e.localizedMessage}")

            getPhoneNumberByGoogleAPI()
        }
    }

    override fun onStart() {
        super.onStart()
        registerBroadcastReceiver()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(smsBroadcastReceiver)
    }

    private fun setMobileNumber(tel: String) {
        binding.etMobileNumber.setText(tel)
    }

    private fun getPhoneNumberByGoogleAPI() {
        val hintRequest = HintRequest.Builder().setPhoneNumberIdentifierSupported(true).build()

        try {
            val intent = Credentials.getClient(this).getHintPickerIntent(hintRequest)
            val googleApiClient = GoogleApiClient.Builder(this).addApi(Auth.CREDENTIALS_API).build()
            val pendingIntent =
                Auth.CredentialsApi.getHintPickerIntent(googleApiClient, hintRequest)
            startIntentSenderForResult(intent.intentSender, RESOLVE_HINT, null, 0, 0, 0)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("TAG", "Could not start hint picker Intent", e)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESOLVE_HINT) {
            if (resultCode == RESULT_OK) {
                val credential: Credential? = data!!.getParcelableExtra(Credential.EXTRA_KEY)
                binding.etMobileNumber.setText(credential!!.id.substring(3))
                // credential.getId(); <-- E.164 format phone number on 10.2.+ devices
            } else if (resultCode == 1002) {

            }
        } else if (requestCode == REQ_USER_CONSENT) {
            if (resultCode == RESULT_OK && data != null) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE).toString()
                val arr = message.split(" ".toRegex(), 10).toTypedArray()
                val firstWord = arr[8]
                val final_str = firstWord.replace(".", "")
                pinview_OTP.setOTP(final_str)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun openVerifyOTPBottomsheet(otp: String) {
        startSmsUserConsent()
        val view = layoutInflater.inflate(R.layout.activity_otp_verification, null)
        val dialog = BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme)
        dialog.setContentView(view)
        dialog.setCancelable(false)
        var mOtp: String = ""

        val btnConfirm = dialog.findViewById<ImageButton>(R.id.ivnextscreen)
        val tvMobile = dialog.findViewById<TextView>(R.id.tvMobile)
        pinview_OTP = dialog.findViewById<OtpTextView>(R.id.otp_view)!!
        pinview_OTP.requestFocusOTP()
        pinview_OTP.otpListener = object : OTPListener {
            override fun onInteractionListener() {

            }

            override fun onOTPComplete(otp: String) {
                mOtp = otp
                if (mOtp.length == 4) {
                    if (otp == mOtp){
                        runNextScreen()
                    } else{
                        showToast(this@VerifyMobileNumberActivity,"Invalid OTP")
                    }
                }
            }

        }
        tvMobile!!.text = binding.etMobileNumber.text.toString()
        val tvChange = dialog.findViewById<TextView>(R.id.tvChange)
        tvChange!!.setOnClickListener {
            dialog.dismiss()
            binding.etMobileNumber.requestFocus()
        }

        btnConfirm?.setOnClickListener(View.OnClickListener {
            if (mOtp == otp){
                runNextScreen()
            } else{
                showToast(this@VerifyMobileNumberActivity,"Invalid OTP")
            }

        })
        dialog.show()
    }

    private fun registerBroadcastReceiver() {
        smsBroadcastReceiver = SmsBroadcastReceiver()
        smsBroadcastReceiver.smsBroadcastReceiverListener =
            object : SmsBroadcastReceiver.SmsBroadcastReceiverListener {
                override fun onSuccess(intent: Intent?) {
                    startActivityForResult(intent, REQ_USER_CONSENT)
                }

                override fun onFailure() {}
            }
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsBroadcastReceiver, intentFilter)
    }

    /*Retrieve OTP*/
    private fun startSmsUserConsent() {

        val client = SmsRetriever.getClient(this)
        //We can add sender phone number or leave it blank
        // I'm adding null here
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener {
            Toast.makeText(applicationContext, "On Success", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(applicationContext, "On OnFailure", Toast.LENGTH_LONG).show()
        }
    }

    private fun runNextScreen() {
        startActivity(Intent(this, LoginActivity::class.java))
    }
}