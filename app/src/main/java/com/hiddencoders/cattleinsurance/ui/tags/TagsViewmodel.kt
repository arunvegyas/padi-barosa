package com.hiddencoders.cattleinsurance.ui.tags

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hiddencoders.cattleinsurance.data.model.TagsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TagsViewmodel @Inject constructor(val repository: TagsRepository):ViewModel() {
    fun getTags():LiveData<TagsModel> = repository.getTagsList()
}