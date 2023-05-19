package com.lcarlosfb.amarillolimon.data

import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lcarlosfb.amarillolimon.data.model.User
import kotlinx.coroutines.tasks.await

class UserRepository {

	private var auth: FirebaseAuth = Firebase.auth
	private var db = Firebase.firestore

	//ResourceRemote<String?>, para retornar el resultado al ViewModel de la solicitud a firebase
	suspend fun signUpUser(email: String, password: String): ResourceRemote<String?> {
		//await, se queda esperando que termine y retornamos el resultado al viewmodel
		return try{
			val result = auth.createUserWithEmailAndPassword(email, password).await()
			ResourceRemote.Success(data = result.user?.uid)
		//Validamos con catch el tipo de error (FirebaseAuthException) que se ejecutó dado que el try no sea satisfactorio
		}catch (e: FirebaseAuthException){
			e.localizedMessage?.let { Log.e("FirebaseAuthEx", it) }
			ResourceRemote.Error(message = e.localizedMessage)
		//Validamos con otro catch el tipo de error (FirebaseNetworkException) errores de red
		}catch (e: FirebaseNetworkException){
			e.localizedMessage?.let { Log.e("FirebaseNetworkEx", it) }
			ResourceRemote.Error(message = e.localizedMessage)
		}
	}

	suspend fun signInUser(email: String, password: String): ResourceRemote<String?> {
		//await, se queda esperando que termine y retornamos el resultado al viewmodel
		return try{
			val result = auth.signInWithEmailAndPassword(email, password).await()
			ResourceRemote.Success(data = result.user?.uid)
			//Validamos con catch el tipo de error (FirebaseAuthException) que se ejecutó dado que el try no sea satisfactorio
		}catch (e: FirebaseAuthException){
			e.localizedMessage?.let { Log.e("FirebaseAuthEx", it) }
			ResourceRemote.Error(message = e.localizedMessage)
			//Validamos con otro catch el tipo de error (FirebaseNetworkException) errores de red
		}catch (e: FirebaseNetworkException){
			e.localizedMessage?.let { Log.e("FirebaseNetworkEx", it) }
			ResourceRemote.Error(message = e.localizedMessage)
		}
	}
	// Para validar si una sesion está iniciada
	fun isSessionActive() : Boolean{
		return auth.currentUser != null
	}

	fun getUIDCurrentUser() : String? {
		return auth.currentUser?.uid
	}

	fun signOut()  {
		auth.signOut()
	}
	// Para cargar el formulario de registro ala DB
	suspend fun createUser(user:User): ResourceRemote<String?> {
		return try{
			user.uid?.let { db.collection("users").document(it).set(user).await() }
			ResourceRemote.Success(data = user.uid)
			//Validamos con catch el tipo de error (FirebaseAuthException) que se ejecutó dado que el try no sea satisfactorio
		}catch (e: FirebaseAuthException){
			e.localizedMessage?.let { Log.e("FirebaseAuthEx", it) }
			ResourceRemote.Error(message = e.localizedMessage)
			//Validamos con otro catch el tipo de error (FirebaseNetworkException) errores de red
		}catch (e: FirebaseNetworkException){
			e.localizedMessage?.let { Log.e("FirebaseNetworkEx", it) }
			ResourceRemote.Error(message = e.localizedMessage)
		}
	}

//QuerySnapshot: tipo de dato que se trae de la collecion de firabse
	//Para traer la informacion a un perfil
	suspend fun loadUserInfo(): ResourceRemote<QuerySnapshot>{
		return try{
			//obtengo cierto usuario
			val result = db.collection("users").get().await()
			ResourceRemote.Success(data = result)
		}catch (e: FirebaseAuthException){
			e.localizedMessage?.let { Log.e("FirebaseAuthEx", it) }
			ResourceRemote.Error(message = e.localizedMessage)
			//Validamos con otro catch el tipo de error (FirebaseNetworkException) errores de red
		}catch (e: FirebaseNetworkException){
			e.localizedMessage?.let { Log.e("FirebaseNetworkEx", it) }
			ResourceRemote.Error(message = e.localizedMessage)
		}

	}
}