package com.hiddencoders.cattleinsurance.data.model

data class TagsModel(
    val code: String,
    val description: String,
    val Data: ArrayList<String>,
    val Tags: ArrayList<Tags>
)

data class Tags(
    val CATID: Int,
    val FARMERNAME: String,
    val TDATE: String,
    val BUFF_COW: String,
    val TAGNO: String,
    val RCTNO: String,
    val ISAPPROVED: Boolean,
    val CREATEDBY: String
)
