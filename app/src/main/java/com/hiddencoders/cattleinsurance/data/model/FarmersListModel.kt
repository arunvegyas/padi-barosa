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
    val BCODE:Int,
    val MandalId:Int,
    val FATHERNAME: String,
    val CCODE: Int,
    val VILLID: Int,
    val CNAME: String,
    val VILLNAME: String,
    val MandalName: String,
    val UID: String,
    val BKCODE:Int,
    val UID_IMAGE:String,
    val BANKPB_IMAGE:String,
    val BANKACCNO: String,
    val CREATEDBY: String,
    val BKNAME:String
)
