package com.lcarlosfb.amarillolimon.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.toObject
import com.lcarlosfb.amarillolimon.data.CategoryRepository
import com.lcarlosfb.amarillolimon.data.ResourceRemote
import com.lcarlosfb.amarillolimon.data.UserRepository
import com.lcarlosfb.amarillolimon.data.model.Category
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

	private val userRepository = UserRepository()
	private val categoryRepository = CategoryRepository()

	private var categoriesList : ArrayList<Category> = ArrayList()


	private val _sessionAdmin: MutableLiveData<Boolean> = MutableLiveData()
	val sessionAdmin : LiveData<Boolean> = _sessionAdmin

	private val _categoryList: MutableLiveData<ArrayList<Category>> = MutableLiveData()
	val categoryList : LiveData<ArrayList<Category>> = _categoryList

	private val _errorMsg: MutableLiveData<String?> = MutableLiveData()
	val errorMsg : LiveData<String?> = _errorMsg

	fun validateAdmin() {
		val uid = userRepository.getUIDCurrentUser()
		if (uid == "mfUqWIswd4Q3ODJCoU6nfKBFfPF3"){
			_sessionAdmin.value = true
		}else{
			//no es admin
		}
	}

	fun loadCategory() {
		categoriesList.clear()
		viewModelScope.launch {
			val result = categoryRepository.loadCategory()
			result.let { resourceRemote ->
				when (resourceRemote){
					is ResourceRemote.Success -> {
						resourceRemote.data?.documents?.forEach{ document ->
							val category = document.toObject<Category>()
							category?.let { categoriesList.add(it) }
						}
						_categoryList.postValue(categoriesList)
					}
					is ResourceRemote.Error -> {
						val msg = result.message
						_errorMsg.postValue(msg)
					}
					else ->{

					}
				}
			}
		}
	}

	fun deleteCategory(category: Category) {
		categoriesList.clear()
		viewModelScope.launch {
			val result = categoryRepository.deleteCategory(category)
			result.let { resourceRemote ->
				when (resourceRemote){
					is ResourceRemote.Success -> {
						resourceRemote.data?.documents?.forEach{ document ->
							val category = document.toObject<Category>()
							category?.let { categoriesList.add(it) }
						}
						_categoryList.postValue(categoriesList)
					}
					is ResourceRemote.Error -> {
						val msg = result.message
						_errorMsg.postValue(msg)
					}
					else ->{

					}
				}
			}
		}
	}

}
