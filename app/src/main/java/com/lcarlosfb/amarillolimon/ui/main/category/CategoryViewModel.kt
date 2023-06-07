package com.lcarlosfb.amarillolimon.ui.main.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.toObject
import com.lcarlosfb.amarillolimon.data.ProductRepository
import com.lcarlosfb.amarillolimon.data.ResourceRemote
import com.lcarlosfb.amarillolimon.data.UserRepository
import com.lcarlosfb.amarillolimon.data.model.Category
import com.lcarlosfb.amarillolimon.data.model.Product
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {

	private val userRepository = UserRepository()
	private val productRepository = ProductRepository()
	private var productsList : ArrayList<Product> = ArrayList()


	private val _sessionAdmin: MutableLiveData<Boolean> = MutableLiveData()
	val sessionAdmin : LiveData<Boolean> = _sessionAdmin

	private val _productList: MutableLiveData<ArrayList<Product>> = MutableLiveData()
	val productList : LiveData<ArrayList<Product>> = _productList

	private val _errorMsg: MutableLiveData<String?> = MutableLiveData()
	val errorMsg : LiveData<String?> = _errorMsg

	fun deleteCategory(product: Product, id: String) {
		productsList.clear()
		viewModelScope.launch {
			val result = productRepository.deleteCategory(product,id)
			result.let { resourceRemote ->
				when (resourceRemote){
					is ResourceRemote.Success -> {
						resourceRemote.data?.documents?.forEach{ document ->
							val product = document.toObject<Product>()
							product?.let { productsList.add(it) }
						}
						_productList.postValue(productsList)
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

	fun loadCategory(id: String) {
		productsList.clear()
		viewModelScope.launch {
			val result = productRepository.loadProduct(id)
			result.let { resourceRemote ->
				when (resourceRemote){
					is ResourceRemote.Success -> {
						resourceRemote.data?.documents?.forEach{ document ->
							val product = document.toObject<Product>()
							product?.let { productsList.add(it) }
						}
						_productList.postValue(productsList)
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

	fun validateAdmin() {
		val uid = userRepository.getUIDCurrentUser()
		if (uid == "mfUqWIswd4Q3ODJCoU6nfKBFfPF3"){
			_sessionAdmin.value = true
		}else{
			//no es admin
		}
	}
}