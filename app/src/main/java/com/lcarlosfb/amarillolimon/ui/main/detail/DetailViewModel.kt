package com.lcarlosfb.amarillolimon.ui.main.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcarlosfb.amarillolimon.data.CartRepository
import com.lcarlosfb.amarillolimon.data.FavoritesRepository
import com.lcarlosfb.amarillolimon.data.ResourceRemote
import com.lcarlosfb.amarillolimon.data.UserRepository
import com.lcarlosfb.amarillolimon.data.model.Cart
import com.lcarlosfb.amarillolimon.data.model.Favorites
import com.lcarlosfb.amarillolimon.data.model.Product
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {
	private val userRepository = UserRepository()
	private val cartRepository = CartRepository()
	private val favoritesRepository = FavoritesRepository()

	private var favoriteItemsList : ArrayList<Favorites> = ArrayList()


	private val _errorMsg : MutableLiveData<String?> = MutableLiveData()
	val errorMsg : LiveData<String?> = _errorMsg

	private val _productAddCartSuccess : MutableLiveData<String?> = MutableLiveData()
	val productAddCartSuccess : LiveData<String?> = _productAddCartSuccess

	private val _flagItemFavorite : MutableLiveData<Boolean> = MutableLiveData()
	val flagItemFavorite : LiveData<Boolean> = _flagItemFavorite

	val uid = userRepository.getUIDCurrentUser()

	fun addToCart(product: Product) {
		viewModelScope.launch {
			val cart = Cart(
				id = product.id,
				shortDescription = product.shortDescription,
				price = product.price,
				picture = product.picture
			)
			val result = uid?.let { cartRepository.saveProductCart(cart, it) }
			result.let {resourceRemote ->
				when (resourceRemote) {
					is ResourceRemote.Success ->{
						_errorMsg.postValue("Producto añadido al carrito de compras")
						_productAddCartSuccess.postValue(resourceRemote.data)
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


	fun searchFavorite(id: String?) {
		favoriteItemsList.clear()
		viewModelScope.launch {
			val result = uid?.let { favoritesRepository.searchFavoriteItems(it) }
			result.let { resourceRemote ->
				when (resourceRemote){
					is ResourceRemote.Success -> {
						resourceRemote.data?.documents?.forEach{ document ->
							if(document.id == id){
								_flagItemFavorite.postValue(true)
							}else{
								_flagItemFavorite.postValue(false)
							}

						}
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

	fun saveFavorite(product: Product) {
//saveItemFavorites
		viewModelScope.launch {
			val favorites = Favorites(
				id = product.id,
				shortDescription = product.shortDescription,
				price = product.price,
				picture = product.picture
			)
			val result = uid?.let { favoritesRepository.saveItemFavorites(favorites, it) }
			result.let {resourceRemote ->
				when (resourceRemote) {
					is ResourceRemote.Success ->{
						_errorMsg.postValue("Producto añadido a favoritos")
						//_productAddCartSuccess.postValue(resourceRemote.data)
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