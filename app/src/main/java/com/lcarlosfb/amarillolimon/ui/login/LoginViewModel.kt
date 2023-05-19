package com.lcarlosfb.amarillolimon.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcarlosfb.amarillolimon.data.ResourceRemote
import com.lcarlosfb.amarillolimon.data.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel(){

	private val userRepository = UserRepository()

	private val _errorMsg: MutableLiveData<String?> = MutableLiveData()
	val errorMsg : LiveData<String?> = _errorMsg

	private val _isSuccessSignIn: MutableLiveData<Boolean> = MutableLiveData()
	val isSuccessSignIn : LiveData<Boolean> = _isSuccessSignIn

	fun validateFields(email: String, password: String) {
		if (email.isEmpty() || password.isEmpty()){
			_errorMsg.value = "Rellenar campos vacíos."
		}else{
			viewModelScope.launch(Dispatchers.IO){
				//resourceRemote es una varibale que devuelve signInUser de UserRepository.kt
				when (val resourceRemote = userRepository.signInUser(email,password)){
					is ResourceRemote.Success ->{
						_isSuccessSignIn.postValue(true)
					}
					is ResourceRemote.Error ->{
						var msg = resourceRemote.message
						when (resourceRemote.message){
							"A network error (such as timeout, interrupted connection or unreachable host) has occurred." -> msg =
								"Revise su conexión de Internet."

							"The email address is badly formatted." -> msg =
								"Ingrese un correo electrónico valido."

							"The password is invalid or the user does not have a password." -> msg =
								"Correo electrónico o contraseña invalida."

							"There is no user record corresponding to this identifier. The user may have been deleted." -> msg =
								"Usuario no registrado."
						}
						_errorMsg.postValue(msg)

					}
					else ->{

					}

				}
			}
		}
	}
}
