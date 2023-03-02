package com.hiddencoders.cattleinsurance.ui.retags

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hiddencoders.cattleinsurance.data.model.ClaimModel
import com.hiddencoders.cattleinsurance.data.model.TagsModel
import com.hiddencoders.cattleinsurance.ui.claims.ClaimsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClaimsViewmodel @Inject constructor(val repository: ClaimsRepository):ViewModel() {
    fun getReTags():LiveData<ClaimModel> = repository.getReTagsList()
}