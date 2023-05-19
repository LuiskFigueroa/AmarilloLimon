package com.lcarlosfb.amarillolimon.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.toObject
import com.lcarlosfb.amarillolimon.data.ResourceRemote
import com.lcarlosfb.amarillolimon.data.UserRepository
import com.lcarlosfb.amarillolimon.data.model.User
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel(){

	private var userRepository = UserRepository()

	private val _errorMsg: MutableLiveData<String?> = MutableLiveData()
	val errorMsg : LiveData<String?> = _errorMsg

	private val _userLoaded: MutableLiveData<User?> = MutableLiveData()
	val userLoaded : LiveData<User?> = _userLoaded


	fun signOut() {
		userRepository.signOut()
	}

	fun loadUserInfo() {
		viewModelScope.launch {
			val result= userRepository.loadUserInfo()
			result.let {resourceRemote ->
				when (resourceRemote){
					is ResourceRemote.Success ->{
						result.data?.documents?.forEach(){document ->
							val user = document.toObject<User>()
								if (user?.uid == userRepository.getUIDCurrentUser()){
									_userLoaded.postValue(user)
								}
						}
					}
					is ResourceRemote.Error ->{
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
