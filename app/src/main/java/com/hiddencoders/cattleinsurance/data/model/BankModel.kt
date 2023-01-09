package com.hiddencoders.cattleinsurance.data.model

data class BanksModel(
    val code: String,
    val description: String,
    val Data: ArrayList<String>,
    val Banks: ArrayList<Banks>
)

data class Banks(
    val BKCODE: Int,
    val SHORTCODE: Int,
    val BKNAME: String,
    val CITY: String,
    val PHONENO: String,
    val ACCNO: String
)
