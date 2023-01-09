package com.hiddencoders.cattleinsurance.ui.tagging

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.hiddencoders.cattleinsurance.data.model.CentersModel
import com.hiddencoders.cattleinsurance.data.model.CommonResponse
import com.hiddencoders.cattleinsurance.data.model.FarmersModel
import com.hiddencoders.cattleinsurance.data.remote.ApiServices
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaggingRepository @Inject constructor(val apiServices: ApiServices) {
    private lateinit var disposable: Disposable
    private lateinit var tagDisposable: Disposable
    private lateinit var centerDisposable: Disposable
    private var getCentersResponse = MutableLiveData<CentersModel>()
    private var getVillageResponse = MutableLiveData<FarmersModel>()
    private var getTagResponse = MutableLiveData<CommonResponse>()

    fun getFarmers(code:Int): LiveData<FarmersModel> {
        disposable = apiServices.getFarmersDetails(code).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getVillageResponse.value = it
            }, {
                Log.d("TAG", "getVillages: ")
            })
        return getVillageResponse
    }

    fun getCentersList(): LiveData<CentersModel> {
        centerDisposable = apiServices.getCenterDetails(0).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getCentersResponse.value = it
            }, {
                Log.d("TAG", "getVillages: ")
            })
        return getCentersResponse
    }

    fun addTaggingFormToServer(jsonObject: JsonObject):LiveData<CommonResponse> {
        tagDisposable = apiServices.newFormToServer(jsonObject).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                    getTagResponse.value = it
            }, {
                Log.d("TAG", "addTaggingFormToServer: ")
            })
        return getTagResponse
    }
}