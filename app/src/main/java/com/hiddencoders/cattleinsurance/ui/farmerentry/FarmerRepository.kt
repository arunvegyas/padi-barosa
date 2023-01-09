package com.hiddencoders.cattleinsurance.ui.farmerentry

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hiddencoders.cattleinsurance.data.model.BanksModel
import com.hiddencoders.cattleinsurance.data.model.BranchModel
import com.hiddencoders.cattleinsurance.data.model.CentersModel
import com.hiddencoders.cattleinsurance.data.model.VillagesModel
import com.hiddencoders.cattleinsurance.data.remote.ApiServices
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FarmerRepository @Inject constructor(val apiServices: ApiServices) {
    private lateinit var disposable: Disposable
    private var getVillageResponse = MutableLiveData<VillagesModel>()
    private lateinit var centerDisposable: Disposable
    private var getCentersResponse = MutableLiveData<CentersModel>()
    private lateinit var bankDisposable: Disposable
    private var getBankResponse = MutableLiveData<BanksModel>()
    private lateinit var branchDisposable: Disposable
    private var getBranchResponse = MutableLiveData<BranchModel>()

    fun getVillages(): LiveData<VillagesModel> {
        disposable = apiServices.getVillages().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getVillageResponse.value = it
            }, {
                Log.d("TAG", "getVillages: ")
            })
        return getVillageResponse
    }
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
    fun getBankList(): LiveData<BanksModel> {
        bankDisposable = apiServices.getBankDetails().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getBankResponse.value = it
            }, {
                Log.d("TAG", "getVillages: ")
            })
        return getBankResponse
    }
    fun getBranchList(code: String): LiveData<BranchModel> {
        branchDisposable = apiServices.getBranchDetails(code).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getBranchResponse.value = it
            }, {
                Log.d("TAG", "getVillages: ")
            })
        return getBranchResponse
    }
}