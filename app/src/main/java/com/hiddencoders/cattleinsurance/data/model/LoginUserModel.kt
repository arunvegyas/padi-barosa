package com.hiddencoders.cattleinsurance.data.model

data class LoginUserModel(
    val code: Int,
    val description: String,
    val Data: ArrayList<LoginUserData>
)

data class LoginUserData(
    val ID: Int,
    val username: String,
    val ACCESSTOKEN: String,
    val BCODE: Int
)
