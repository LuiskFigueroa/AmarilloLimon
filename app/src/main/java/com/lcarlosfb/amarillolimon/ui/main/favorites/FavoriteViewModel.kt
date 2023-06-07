package com.lcarlosfb.amarillolimon.ui.main.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.toObject
import com.lcarlosfb.amarillolimon.data.FavoritesRepository
import com.lcarlosfb.amarillolimon.data.ResourceRemote
import com.lcarlosfb.amarillolimon.data.UserRepository
import com.lcarlosfb.amarillolimon.data.model.Favorites
import kotlinx.coroutines.launch

class FavoriteViewModel : ViewModel() {
	private val userRepository = UserRepository()
	private val favoritesRepository = FavoritesRepository()
	private var favoriteItemsList : ArrayList<Favorites> = ArrayList()

	val uid = userRepository.getUIDCurrentUser()

	private val _favoriteList: MutableLiveData<ArrayList<Favorites>> = MutableLiveData()
	val favoriteList : LiveData<ArrayList<Favorites>> = _favoriteList

	private val _errorMsg: MutableLiveData<String?> = MutableLiveData()
	val errorMsg : LiveData<String?> = _errorMsg

	fun deleteFavoriteItem(favorite: Favorites) {
		favoriteItemsList.clear()
		viewModelScope.launch {
			val result = uid?.let { favoritesRepository.deleteFavoriteItem(favorite, it) }
			result.let { resourceRemote ->
				when (resourceRemote){
					is ResourceRemote.Success -> {
						resourceRemote.data?.documents?.forEach{ document ->
							val favorite = document.toObject<Favorites>()
							favorite?.let { favoriteItemsList.add(it) }
						}
						_favoriteList.postValue(favoriteItemsList)
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


	fun loadFavorites() {
		favoriteItemsList.clear()
		viewModelScope.launch {
			val result = uid?.let { favoritesRepository.loadFavoritesItems(it) }
			result.let { resourceRemote ->
				when (resourceRemote){
					is ResourceRemote.Success -> {
						resourceRemote.data?.documents?.forEach{ document ->
							val favorite = document.toObject<Favorites>()
							favorite?.let { favoriteItemsList.add(it) }
						}
						_favoriteList.postValue(favoriteItemsList)
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