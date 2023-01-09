package com.hiddencoders.cattleinsurance.data.model

data class CentersModel(
    val code: String,
    val description: String,
    val Data: ArrayList<String>,
    val Centers: ArrayList<Centers>
)

data class Centers(val CNAME: String, val CCODE: Int)
