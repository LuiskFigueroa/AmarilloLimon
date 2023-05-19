package com.lcarlosfb.amarillolimon.data

//Gestionamos los errores presentados al hacer una solicitud a Firebase
sealed class ResourceRemote<T> (
	//Tendrá 4 atributos
	// 1. Informacion que quiero enviar
	var data: T? = null,
	// 2. Un mensaje (Respuesta del servidor)
	var message:String? = null,
	// 3. Status
	var status:Status? = null,
	// 4. Codigo de error que contiene un mensaje http
	var errorCode:Int? = null
){
	class Success<T>(data: T?):ResourceRemote<T>(data = data, status = Status.Success)

	class Loading<T>(message: String? = ""):ResourceRemote<T>(message= message, status = Status.Loading)

	class Error<T>(errorCode: Int? = null, message: String? = null) : ResourceRemote<T>(errorCode= errorCode,
	message = message, status = Status.Error)
}

//Genera una secuencia desde 0, validamos Status como: Success,Error o Loading
enum class Status{
	Success,
	Error,
	Loading
}
