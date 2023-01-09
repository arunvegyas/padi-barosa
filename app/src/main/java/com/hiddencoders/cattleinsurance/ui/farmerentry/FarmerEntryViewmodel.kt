package com.hiddencoders.cattleinsurance.ui.farmerentry

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
class FarmerEntryViewmodel @Inject constructor(
    val apiServices: ApiServices,
    val repository: FarmerRepository
) : ViewModel() {

    var villageName: String? = null
    var farmerCode: String? = null
    var centerName: String? = null
    var farmerName: String? = null
    var fatherName: String? = null
    var contactNumber: String? = null
    var adharNo: String? = null
    var bankAcNo: String? = null
    var bankName: String? = null
    var branch: String? = null
    var uploadAdhar: String? = null
    var bcode: Int? = 0
    var username: String? = null
    var uploadBankPassbook: String? = null

    private lateinit var disposable: Disposable
    private var getCommomResponse = MutableLiveData<CommonResponse>()

    var authListner: AuthListner? = null
    private fun isFramerEntryFormValid(): Boolean {
        if (villageName.isNullOrEmpty()) {
            authListner!!.onFailure("Please select village name")
            return false
        } else if (centerName.isNullOrEmpty()) {
            authListner!!.onFailure("Please select center name")
            return false
        } else if (farmerName.isNullOrEmpty()) {
            authListner!!.onFailure("Please enter farmer name")
            return false
        } else if (fatherName.isNullOrEmpty()) {
            authListner!!.onFailure("Please enter father name")
            return false
        } else if (contactNumber.isNullOrEmpty()) {
            authListner!!.onFailure("please enter contact number")
            return false
        } else if (adharNo.isNullOrEmpty() || adharNo!!.length < 12) {
            authListner!!.onFailure("Please enter valid adhar No")
        } else if (bankAcNo.isNullOrEmpty()) {
            authListner!!.onFailure("Please enter Bank account No")
            return false
        } else if (bankName.isNullOrEmpty()) {
            authListner!!.onFailure("Please enter you bank name")
            return false
        } else if (branch.isNullOrEmpty()) {
            authListner!!.onFailure("Please enter you branch name")
            return false
        } else if (uploadAdhar.isNullOrEmpty()) {
            authListner!!.onFailure("Please upload your Adhar card image")
            return false
        } else if (uploadBankPassbook.isNullOrEmpty()) {
            authListner!!.onFailure("Please upload your Bank Passbook")
            return false
        }
        return true
    }

    fun addFarmerFormToServer(): LiveData<CommonResponse> {
        if (isFramerEntryFormValid()) {
            val jsonObject = JsonObject()
            jsonObject.addProperty("farmername", farmerName)
            jsonObject.addProperty("fathername", fatherName)
            jsonObject.addProperty("username", username)
            jsonObject.addProperty("CONTACTNO", contactNumber)
            jsonObject.addProperty("ccode", centerName)
            jsonObject.addProperty("uid", adharNo)
            jsonObject.addProperty("bcode", bcode)
            jsonObject.addProperty("BANKACCNO", bankAcNo)
            jsonObject.addProperty("BKCODE", branch)
            jsonObject.addProperty("UID_IMAGE", uploadAdhar)
            jsonObject.addProperty("BANKPB_IMAGE", uploadBankPassbook)
            disposable = apiServices.addFarmerToServer(jsonObject).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    getCommomResponse.value = it
                    authListner!!.onSuccess()
                }, {
                    authListner!!.onFailure(it.localizedMessage)
                })
            authListner!!.onSuccess()
        }
        return getCommomResponse
    }

    fun getVillages(): LiveData<VillagesModel> = repository.getVillages()
    fun getCenters(code: Int): LiveData<CentersModel> = repository.getCentersList(code)
    fun getBanks(): LiveData<BanksModel> = repository.getBankList()
    fun getBranches(code: String): LiveData<BranchModel> = repository.getBranchList(code)
}