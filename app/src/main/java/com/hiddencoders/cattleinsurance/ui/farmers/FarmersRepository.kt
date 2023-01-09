package com.hiddencoders.cattleinsurance.ui.farmers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hiddencoders.cattleinsurance.data.model.CentersModel
import com.hiddencoders.cattleinsurance.data.model.FarmersListModel
import com.hiddencoders.cattleinsurance.data.remote.ApiServices
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FarmersRepository @Inject constructor(val apiServices: ApiServices) {
    private lateinit var centerDisposable: Disposable
    private var getCentersResponse = MutableLiveData<CentersModel>()
    private lateinit var farmerDisposable: Disposable
    private var getFarmerResponse = MutableLiveData<FarmersListModel>()
    fun getCentersList(code: Int): LiveData<CentersModel> {
        centerDisposable = apiServices.getCenterDetails(code).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getCentersResponse.value = it
            }, {
                Log.d("TAG", "getVillages: ")
            })
        return getCentersResponse
    }

    fun getCenterFarmerList(code: Int, name: String): LiveData<FarmersListModel> {
        farmerDisposable =
            apiServices.getFarmersListDetails(code, name).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    getFarmerResponse.value = it
                }, {
                    Log.d("TAG", "getCenterFarmerList: ")
                })
        return getFarmerResponse
    }
}