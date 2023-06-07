package com.lcarlosfb.amarillolimon.ui.main.home.newcategory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcarlosfb.amarillolimon.data.CategoryRepository
import com.lcarlosfb.amarillolimon.data.ResourceRemote
import com.lcarlosfb.amarillolimon.data.model.Category
import kotlinx.coroutines.launch

class NewCategoryViewModel : ViewModel() {

	private val categoryRepository = CategoryRepository()

	private val _errorMsg: MutableLiveData<String?> = MutableLiveData()
	val errorMsg: LiveData<String?> = _errorMsg

	private val _createCategorySuccess: MutableLiveData<String?> = MutableLiveData()
	val createCategorySuccess: LiveData<String?> = _createCategorySuccess


	fun createCategory(name: String) {
		if (name.isEmpty()) {
			_errorMsg.value = "Rellenar campos vacíos"
		} else {
			viewModelScope.launch {
				val category = Category(
					id = name,
					name = name,
					picture = null,
				)
				val result = categoryRepository.saveCategory(category)
				result.let { resourceRemote ->
					when (resourceRemote) {
						is ResourceRemote.Success -> {
							_errorMsg.postValue("Categoría creada")
							_createCategorySuccess.postValue(resourceRemote.data)
						}
						is ResourceRemote.Error -> {
							val msg = resourceRemote.message
							_errorMsg.postValue(msg)
						}
						else -> {

						}
					}
				}
			}
		}
	}
}
