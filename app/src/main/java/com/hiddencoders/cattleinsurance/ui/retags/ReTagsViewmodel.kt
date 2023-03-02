package com.hiddencoders.cattleinsurance.ui.retags

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hiddencoders.cattleinsurance.data.model.TagsModel
import com.hiddencoders.cattleinsurance.ui.claims.ClaimsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReTagsViewmodel @Inject constructor(val repository: ReTagsRepository):ViewModel() {
    fun getReTags():LiveData<TagsModel> = repository.getReTagsList()
}