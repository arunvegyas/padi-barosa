package com.hiddencoders.cattleinsurance.ui.tagging

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.hiddencoders.cattleinsurance.data.model.*
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
    private lateinit var tagsDisposable: Disposable
    private var getCentersResponse = MutableLiveData<CentersModel>()
    private var getVillageResponse = MutableLiveData<FarmersModel>()
    private var getTagResponse = MutableLiveData<CommonResponse>()
    private var getTagBtIdResponse = MutableLiveData<TagsModel>()
    private var getTagByTagNoResponse = MutableLiveData<TagsModel>()
    private var getClaimByTagNoResponse = MutableLiveData<ClaimsDataModel>()

    fun getFarmers(code: Int): LiveData<FarmersModel> {
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

    fun getTagById(id: String): LiveData<TagsModel> {
        tagDisposable = apiServices.getTagDataById(id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getTagBtIdResponse.value = it
            }, {
                Log.d("TAG", "getTagById: ")
            })
        return getTagBtIdResponse
    }

    fun getTagByTagNo(id: String): LiveData<TagsModel> {
        tagDisposable = apiServices.getTagDataByTagNo(id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getTagByTagNoResponse.value = it
            }, {
                Log.d("TAG", "getTagById: ")
            })
        return getTagByTagNoResponse
    }

    fun getClaimByTagNo(id: String): LiveData<ClaimsDataModel> {
        tagDisposable = apiServices.getClaimDataByTagNo(id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getClaimByTagNoResponse.value = it
            }, {
                Log.d("TAG", "getTagById: ")
            })
        return getClaimByTagNoResponse
    }
    fun addTaggingFormToServer(jsonObject: JsonObject): LiveData<CommonResponse> {
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