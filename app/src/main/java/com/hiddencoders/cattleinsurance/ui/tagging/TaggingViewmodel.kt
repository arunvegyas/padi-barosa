package com.hiddencoders.cattleinsurance.ui.tagging

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.hiddencoders.cattleinsurance.data.AuthListner
import com.hiddencoders.cattleinsurance.data.model.*
import com.hiddencoders.cattleinsurance.data.remote.ApiServices
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class TaggingViewmodel @Inject constructor(
    val apiServices: ApiServices,
    val repository: TaggingRepository
) : ViewModel() {

    private lateinit var tagDisposable: Disposable
    private var getTagResponse = MutableLiveData<CommonResponse>()
    private var updateTag = MutableLiveData<UpdateTagResponse>()

    var selectedDate: String? = null
    var selectFarmer: String? = null
    var milkFeild: String? = null
    var categoryId:String? = null
    var tagNo: String? = null
    var rctNo: String? = null
    var buffaloType: String? = null
    var breedType: String? = null
    var localGraded: String? = null
    var animalStatus: String? = null
    var uploadImage1: String? = null
    var uploadImage2: String? = null
    var bcode: Int? = 0
    var username: String? = null
    var authListener: AuthListner? = null

    private fun isTaggingFormValid(): Boolean {
        if (selectFarmer.isNullOrEmpty() || selectFarmer == "0") {
            authListener!!.onFailure("Please select farmer name")
            return false
        }  else if (tagNo.isNullOrEmpty()) {
            authListener!!.onFailure("Please enter TAG No")
            return false
        } else if (tagNo!!.length < 6){
            authListener!!.onFailure("Please enter valid Tag No")
            return false
        } else if (rctNo.isNullOrEmpty()) {
            authListener!!.onFailure("Please enter Receipt No")
            return false
        } else if (buffaloType.isNullOrEmpty()) {
            authListener!!.onFailure("Please select Buffalo or Cow")
            return false
        } else if (localGraded.isNullOrEmpty()) {
            authListener!!.onFailure("Please enter premium")
            return false
        } else if (animalStatus.isNullOrEmpty()) {
            authListener!!.onFailure("Please Milking or Pregnant")
            return false
        } else if (uploadImage1.isNullOrEmpty()) {
            authListener!!.onFailure("Please Upload Image1")
            return false
        } else if (uploadImage2.isNullOrEmpty()) {
            authListener!!.onFailure("Please Upload Image2")
        }
        return true
    }

    fun addTaggingFormToServer(): LiveData<CommonResponse> {

        if (isTaggingFormValid()) {
            val jsonObject = JsonObject()
            jsonObject.addProperty("FARMERID", selectFarmer)
            jsonObject.addProperty("TDATE", selectedDate)
            jsonObject.addProperty("MILK_YIELD", milkFeild)
            jsonObject.addProperty("BUFF_COW", buffaloType)
            jsonObject.addProperty("BREED", breedType)
            jsonObject.addProperty("PREMIUM", localGraded)
            jsonObject.addProperty("ANIMAL_STATUS", animalStatus)
            jsonObject.addProperty("bcode", bcode)
            jsonObject.addProperty("UserName", username)
            jsonObject.addProperty("IMAGE1_IMAGE", uploadImage1)
            jsonObject.addProperty("IMAGE2_IMAGE", uploadImage2)
            jsonObject.addProperty("TAGNO", tagNo)
            jsonObject.addProperty("RCTNO", rctNo)
            tagDisposable = apiServices.newFormToServer(jsonObject).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.code==0){
                        getTagResponse.value = it
                        authListener!!.onSuccess()
                    } else{
                        authListener!!.onFailure(it.description)
                    }

                }, {
                    Log.d("TAG", "addTaggingFormToServer: ")
                    authListener!!.onFailure(it.message.toString())
                })
        }
        return getTagResponse

    }
    fun getTagById(id:String):LiveData<TagsModel> = repository.getTagById(id)
    fun getTagByTagNo(id: String):LiveData<TagsModel> = repository.getTagByTagNo(id)
    fun getClaimByTagNo(id: String):LiveData<ClaimsDataModel> = repository.getClaimByTagNo(id)
    fun editTagFormOnServer(): LiveData<CommonResponse> {

        if (isTaggingFormValid()) {
            val jsonObject = JsonObject()
            jsonObject.addProperty("FARMERID", selectFarmer)
            jsonObject.addProperty("CATID",categoryId)
            jsonObject.addProperty("TDATE", selectedDate)
            jsonObject.addProperty("MILK_YIELD", milkFeild)
            jsonObject.addProperty("BUFF_COW", buffaloType)
            jsonObject.addProperty("BREED", breedType)
            jsonObject.addProperty("PREMIUM", localGraded)
            jsonObject.addProperty("ANIMAL_STATUS", animalStatus)
            jsonObject.addProperty("bcode", bcode)
            jsonObject.addProperty("UserName", username)
            jsonObject.addProperty("IMAGE1_IMAGE", uploadImage1)
            jsonObject.addProperty("IMAGE2_IMAGE", uploadImage2)
            jsonObject.addProperty("TAGNO", tagNo)
            jsonObject.addProperty("RCTNO", rctNo)
            tagDisposable = apiServices.editFormOnServer(jsonObject).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.code == 0){
                        getTagResponse.value = it
                        authListener!!.onSuccess()
                    } else{
                        authListener!!.onFailure(it.description)
                    }

                }, {
                    Log.d("TAG", "addTaggingFormToServer: ")
                })
        }
        return getTagResponse

    }
    fun getFarmers(code: Int): LiveData<FarmersModel> = repository.getFarmers(code)
    fun getCenters(): LiveData<CentersModel> = repository.getCentersList()
}