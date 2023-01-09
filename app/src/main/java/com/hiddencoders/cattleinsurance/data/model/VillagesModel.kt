package com.hiddencoders.cattleinsurance.data.model

data class VillagesModel(val code: Int, val description: String, val Data: ArrayList<String>,val Villages:ArrayList<Villages>)
data class Villages(val VillName: String,val VillID:Int) {
    override fun toString(): String {
        return "($VillID) $VillName"
    }
}
