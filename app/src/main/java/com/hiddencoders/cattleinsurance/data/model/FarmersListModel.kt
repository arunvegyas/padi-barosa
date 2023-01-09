package com.hiddencoders.cattleinsurance.data.model

data class FarmersListModel(
    val code: String,
    val description: String,
    val Data: ArrayList<String>,
    val Farmers: ArrayList<FarmersData>
)

data class FarmersData(
    val FARMERNAME: String,
    val FARMERID: Int,
    val CONTACTNO: String,
    val FATHERNAME: String,
    val CCODE: Int,
    val VILLID: Int,
    val CNAME: String,
    val VILLNAME: String,
    val MandalName: String,
    val UID: String,
    val BANKACCNO: String,
    val CREATEDBY: String
)
