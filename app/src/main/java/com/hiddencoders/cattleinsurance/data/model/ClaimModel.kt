package com.hiddencoders.cattleinsurance.data.model

data class ClaimModel(
    val code: String,
    val description: String,
    val Data: ArrayList<String>,
    val Tags: ArrayList<ClaimTags>
)

data class ClaimTags(
    val CATID: Int,
    val FARMERNAME: String,
    val INC_DATE:String,
    val CLAIM_DATE: String,
    val BCODE:Int,
    val ccode:Int,
    val PREMIUM:Int,
    val ANIMAL_STATUS:String,
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
