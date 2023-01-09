package com.hiddencoders.cattleinsurance.data

interface AuthListner {
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}