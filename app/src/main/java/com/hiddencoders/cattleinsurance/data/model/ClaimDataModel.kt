package com.hiddencoders.cattleinsurance.data.model

data class ClaimsDataModel(
    val code: String,
    val description: String,
    val Data: ArrayList<String>,
    val Claim: ArrayList<Claims>
)

data class Claims(
    val CATID: Int,
    val FARMERNAME: String,
    val FARMERID:Int,
    val TDATE: String,
    val BUFF_COW: String,
    val BCODE:Int,
    val ccode:Int,
    val PREMIUM:String,
    val ANIMAL_STATUS:Int,
    val MILK_YIELD:String,
    val IMAGE1_IMAGE:String,
    val IMAGE2_IMAGE:String,
    val IMAGE1_FILENAME:String,
    val IMAGE2_FILENAME:String,
    val TAGNO: String,
    val BREED:String,
    val RCTNO: String,
    val ISAPPROVED: Boolean,
    val CREATEDBY: String
)
