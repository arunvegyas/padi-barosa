package com.hiddencoders.cattleinsurance.ui.utilis

import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.view.Window
import android.widget.TextView
import com.hiddencoders.cattleinsurance.R

object Utilities {
    fun showAlertDialog(context: Context?, contentName: String?, message: String?) {
        val showAlert = Dialog(context!!, R.style.DialogSlideAnim)
        showAlert.requestWindowFeature(Window.FEATURE_NO_TITLE)
        showAlert.setCancelable(false)
        showAlert.setContentView(R.layout.dialog_verify_phone)
        val contentNameTextView = showAlert.findViewById<TextView>(R.id.tvUserMobileNumber)
        val tvMessageTextView = showAlert.findViewById<TextView>(R.id.tvMessage)
        contentNameTextView.text = contentName
        tvMessageTextView.text = message
        showAlert.show()
        Handler().postDelayed({
            showAlert.dismiss()
        },2000)
    }
}