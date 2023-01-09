package com.hiddencoders.cattleinsurance.ui.farmers

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hiddencoders.cattleinsurance.data.model.CentersModel
import com.hiddencoders.cattleinsurance.data.model.FarmersListModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FarmersViewmodel @Inject constructor(val repository: FarmersRepository) : ViewModel() {
    fun getCenters(code: Int): LiveData<CentersModel> = repository.getCentersList(code)
    fun getCenterFarmerList(code: Int, name: String): LiveData<FarmersListModel> =
        repository.getCenterFarmerList(code, name)
}