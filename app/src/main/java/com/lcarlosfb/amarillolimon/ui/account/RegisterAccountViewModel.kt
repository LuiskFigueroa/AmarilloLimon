package com.lcarlosfb.amarillolimon.ui.account

import androidx.core.util.PatternsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcarlosfb.amarillolimon.data.ResourceRemote
import com.lcarlosfb.amarillolimon.data.UserRepository
import com.lcarlosfb.amarillolimon.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class RegisterAccountViewModel : ViewModel() {

	private val userRepository = UserRepository()

	val edtNameError: MutableLiveData<String> by lazy {
		MutableLiveData<String>()
	}
	val edtlastNameError: MutableLiveData<String> by lazy {
		MutableLiveData<String>()
	}
	val edtEmailError: MutableLiveData<String> by lazy {
		MutableLiveData<String>()
	}
	val edtPhoneError: MutableLiveData<String> by lazy {
		MutableLiveData<String>()
	}
	val edtDocIdError: MutableLiveData<String> by lazy {
		MutableLiveData<String>()
	}
	val edtGenderError: MutableLiveData<String> by lazy {
		MutableLiveData<String>()
	}
	val edtPasswordError: MutableLiveData<String> by lazy {
		MutableLiveData<String>()
	}
	val edtrepPasswordError: MutableLiveData<String> by lazy {
		MutableLiveData<String>()
	}
	val verificarPasswordError : MutableLiveData<String> by lazy{
		MutableLiveData<String>()
	}

	val toastError: MutableLiveData<String?> by lazy {
		MutableLiveData<String?>()
	}
/*	private val _errorMsg: MutableLiveData<String?> = MutableLiveData()
	val errorMsg : LiveData<String?> = _errorMsg */

	private val _isSuccessSignUp: MutableLiveData<Boolean> = MutableLiveData()
	val isSuccessSignUp : LiveData<Boolean> = _isSuccessSignUp

	fun registerPerson(name:String,lastName:String,email:String,phone:String,
	                   gender:String,docId:String,password:String,repPassword:String) {

		val passwordRegex = Pattern.compile(
			"^" +
					"(?=\\S+$)" +        //No admite espacios.
					".{6,}" +            //Minimo 6 caracteres.
					"$"
		)

		if (name.isEmpty()) {
			edtNameError.value = "Ingrese su Nombre"
		}
		if (lastName.isEmpty()) {
			edtlastNameError.value = "Ingrese su Apellido"
		}
		if (email.isEmpty()) {
			edtEmailError.value = "Ingrese su correo electronico"
		}
		if (phone.isEmpty()) {
			edtPhoneError.value = "Ingrese su número celular"
		}
		if (docId.isEmpty()) {
			edtDocIdError.value = "Ingrese su número celular"
		}
		if (gender.isEmpty()) {
			edtGenderError.value = "Ingrese su género"
		}
		if (password.isEmpty()) {
			edtPasswordError.value = "Ingrese contraseña"
		}
		if (repPassword.isEmpty()) {
			edtrepPasswordError.value = "Ingrese contraseña"
		}


		if (name.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() ||
				docId.isEmpty() || password.isEmpty() || repPassword.isEmpty()){
			toastError.value = "Rellenar campos vacíos."
		}else if(!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()){
			edtEmailError.value = "Ingrese un correo electronico valido"
			toastError.value = "Ingrese un correo electronico valido"
		}else if(!passwordRegex.matcher(password).matches() ){
			edtPasswordError.value = "La contraseña debe tener:\n" +
					" *Minimo 6 caracteres\n" +
					" *Sin espacios"
			toastError.value = "Contraseña invalida"
		}else if (password != repPassword) {
			verificarPasswordError.value = "Verificar contraseña"
			toastError.value = "Las contraseñas no son iguales"
		}else{
			viewModelScope.launch(Dispatchers.IO) {
				val result = userRepository.signUpUser(email, password)
				//Primero extraemos el resultado del estado de ResourceRemote
				result.let {resourceRemote ->
					when (resourceRemote){
						is ResourceRemote.Success ->{
							//Aqui debemos crear el usuario en base de datosfirebase y un true al fragmento para que vaya al login
							//User es el modelo creado
							val user = User(
								uid = resourceRemote.data,
								name = name,
								lastName = lastName,
								email = email,
								numberPhone = phone,
								gender = gender,
								docId = docId
							)
							createUser(user)
							//_isSuccessSignUp.postValue(true)
						}
						is ResourceRemote.Error ->{
							var msg = resourceRemote.message
							when (resourceRemote.message){
								"A network error (such as timeout, interrupted connection or unreachable host) has occurred." -> msg =
									"Revise su conexión de Internet"
								"The email address is already in use by another account." -> msg =
									"Ya existe una cuenta con ese correo electrónico"
								"The email address is badly formatted." -> msg =
									"Ingrese un correo electrónico valido"
							}
							//.postValue cuando trabajamos con coroutinas
							toastError.postValue(msg)
						}
						else ->{

						}

					}
				}
			}
		}

	}

	private fun createUser(user: User) {
		viewModelScope.launch(Dispatchers.IO){
			val result = userRepository.createUser(user)
			result.let { resourceRemote ->
				when (resourceRemote){
					is ResourceRemote.Success ->{
						_isSuccessSignUp.postValue(true)
						userRepository.signOut()
						//toastError.postValue("Registro exitoso")
					}
					is ResourceRemote.Error -> {
						val msg = result.message
						toastError.postValue(msg)
					}
					else ->{

					}
				}
			}
		}
	}
}
