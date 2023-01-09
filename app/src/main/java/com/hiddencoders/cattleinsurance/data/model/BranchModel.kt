package com.hiddencoders.cattleinsurance.data.model

data class BranchModel(
    val code: String,
    val description: String,
    val Data: ArrayList<String>,
    val Branch: ArrayList<Branch>
)

data class Branch(
    val BKCODE: Int,
    val SHORTCODE: String,
    val BKNAME: String,
    val CITY: String,
    val PHONENO: String,
    val ACCNO: String
)
