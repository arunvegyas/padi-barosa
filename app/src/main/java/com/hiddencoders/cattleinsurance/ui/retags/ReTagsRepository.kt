package com.hiddencoders.cattleinsurance.ui.retags

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hiddencoders.cattleinsurance.data.model.TagsModel
import com.hiddencoders.cattleinsurance.data.remote.ApiServices
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReTagsRepository @Inject constructor(val apiServices: ApiServices) {
    private lateinit var disposable: Disposable
    private var getResponse = MutableLiveData<TagsModel>()

    fun getReTagsList(): LiveData<TagsModel> {
        disposable = apiServices.getReTagData().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getResponse.value = it
            }, {
                Log.d("TAG", "getTagsList: ")
            })
        return getResponse
    }
}