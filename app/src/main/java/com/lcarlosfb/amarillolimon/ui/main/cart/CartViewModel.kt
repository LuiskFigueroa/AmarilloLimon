package com.lcarlosfb.amarillolimon.ui.main.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.toObject
import com.lcarlosfb.amarillolimon.data.CartRepository
import com.lcarlosfb.amarillolimon.data.ResourceRemote
import com.lcarlosfb.amarillolimon.data.UserRepository
import com.lcarlosfb.amarillolimon.data.model.Cart
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {

	private val userRepository = UserRepository()
	private val cartRepository = CartRepository()
	private var cartItemsList : ArrayList<Cart> = ArrayList()


	private val _cartList: MutableLiveData<ArrayList<Cart>> = MutableLiveData()
	val cartList : LiveData<ArrayList<Cart>> = _cartList

	private val _errorMsg: MutableLiveData<String?> = MutableLiveData()
	val errorMsg : LiveData<String?> = _errorMsg


	val uid = userRepository.getUIDCurrentUser()


	fun deleteCartItem(cart: Cart) {
		cartItemsList.clear()
		viewModelScope.launch {
			val result = uid?.let { cartRepository.deleteCartItem(cart, it) }
			result.let { resourceRemote ->
				when (resourceRemote){
					is ResourceRemote.Success -> {
						resourceRemote.data?.documents?.forEach{ document ->
							val cart = document.toObject<Cart>()
							cart?.let { cartItemsList.add(it) }
						}
						_cartList.postValue(cartItemsList)
					}
					is ResourceRemote.Error -> {
						val msg = result?.message
						_errorMsg.postValue(msg)
					}
					else ->{

					}
				}
			}
		}
	}

	fun loadCart() {
		cartItemsList.clear()
		viewModelScope.launch {
			val result = uid?.let { cartRepository.loadCartItems(it) }
			result.let { resourceRemote ->
				when (resourceRemote){
					is ResourceRemote.Success -> {
						resourceRemote.data?.documents?.forEach{ document ->
							val cart = document.toObject<Cart>()
							cart?.let { cartItemsList.add(it) }
						}
						_cartList.postValue(cartItemsList)
					}
					is ResourceRemote.Error -> {
						val msg = result?.message
						_errorMsg.postValue(msg)
					}
					else ->{

					}
				}
			}
		}
	}
}