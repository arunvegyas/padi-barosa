package com.hiddencoders.cattleinsurance.data.model

data class FarmersModel(
    val code: String,
    val description: String,
    val Data: ArrayList<String>,
    val Farmers: ArrayList<Farmers>
)

data class Farmers(val FARMERNAME: String, val FARMERID: Int)
