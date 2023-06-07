package com.lcarlosfb.amarillolimon.ui.main.category.newproduct

import android.app.ProgressDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.lcarlosfb.amarillolimon.data.ProductRepository
import com.lcarlosfb.amarillolimon.data.ResourceRemote
import com.lcarlosfb.amarillolimon.data.model.Product
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class NewProductViewModel : ViewModel() {

	private val productRepository = ProductRepository()

	private val _errorMsg : MutableLiveData<String?> = MutableLiveData()
	val errorMsg : LiveData<String?> = _errorMsg

	private val _createProductSuccess : MutableLiveData<String?> = MutableLiveData()
	val createProductSuccess : LiveData<String?> = _createProductSuccess


	fun createProduct(id: String, shortDescription: String, description: String, price: String, idCategory:String) {
		if (id.isEmpty() || shortDescription.isEmpty() || description.isEmpty() || price.isEmpty()){
			_errorMsg.value = "Rellenar campos vacíos"
		}else{
			viewModelScope.launch {
				val product = Product(
					id = id,
					shortDescription = shortDescription,
					description= description,
					price = price,
					picture = null
				)
				val result = productRepository.saveProduct(product, idCategory)
				result.let {resourceRemote ->
					when (resourceRemote) {
						is ResourceRemote.Success ->{
							_errorMsg.postValue("Producto creado")
							_createProductSuccess.postValue(resourceRemote.data)
						}
						is ResourceRemote.Error ->{
							val msg = resourceRemote.message
							_errorMsg.postValue(msg)
						}
						else->{

						}
					}
				}
			}
		}
	}



}
