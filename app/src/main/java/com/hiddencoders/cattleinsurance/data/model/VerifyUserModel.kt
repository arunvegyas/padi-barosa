package com.hiddencoders.cattleinsurance.data.model

data class VerifyUserModel(val code: String, val description: String, val Data: ArrayList<VerifyData>)
data class VerifyData(val otp: String)
