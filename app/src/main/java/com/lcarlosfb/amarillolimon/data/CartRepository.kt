package com.lcarlosfb.amarillolimon.data

import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lcarlosfb.amarillolimon.data.model.Cart
import com.lcarlosfb.amarillolimon.data.model.Category
import com.lcarlosfb.amarillolimon.data.model.Product
import kotlinx.coroutines.tasks.await

class CartRepository {
	private var db = Firebase.firestore

	suspend fun saveProductCart(cart: Cart, uid: String): ResourceRemote<String?> {
		return try{
			cart.id?.let { db.collection("users").document(uid)
				.collection("carrito").document(it).set(cart).await() }
			ResourceRemote.Success(data = cart.id)
			//Validamos con catch el tipo de error (FirebaseAuthException) que se ejecutó dado que el try no sea satisfactorio
		}catch (e: FirebaseFirestoreException){
			e.localizedMessage?.let { Log.e("FirebaseFirestoreEx", it) }
			ResourceRemote.Error(message = e.localizedMessage)
			//Validamos con otro catch el tipo de error (FirebaseNetworkException) errores de red
		}catch (e: FirebaseNetworkException){
			e.localizedMessage?.let { Log.e("FirebaseNetworkEx", it) }
			ResourceRemote.Error(message = e.localizedMessage)
		}
	}

	suspend fun loadCartItems(uid: String): ResourceRemote<QuerySnapshot?>{
		return try{
			//obtengo cierto usuario
			val result = db.collection("users").document(uid)
				.collection("carrito").get().await()
			ResourceRemote.Success(data = result)
		}catch (e: FirebaseFirestoreException){
			e.localizedMessage?.let { Log.e("FirebaseFirestoreEx", it) }
			ResourceRemote.Error(message = e.localizedMessage)
			//Validamos con otro catch el tipo de error (FirebaseNetworkException) errores de red
		}catch (e: FirebaseNetworkException){
			e.localizedMessage?.let { Log.e("FirebaseNetworkEx", it) }
			ResourceRemote.Error(message = e.localizedMessage)
		}
	}

	suspend fun deleteCartItem(cart: Cart, uid: String): ResourceRemote<QuerySnapshot?>{
		return try{
			//obtengo cierto usuario
			cart.id?.let { db.collection("users").document(uid)
				.collection("carrito").document(it).delete().await() }
			val result = db.collection("users").document(uid)
				.collection("carrito").get().await()
			ResourceRemote.Success(data = result )
		}catch (e: FirebaseFirestoreException){
			e.localizedMessage?.let { Log.e("FirebaseFirestoreEx", it) }
			ResourceRemote.Error(message = e.localizedMessage)
			//Validamos con otro catch el tipo de error (FirebaseNetworkException) errores de red
		}catch (e: FirebaseNetworkException){
			e.localizedMessage?.let { Log.e("FirebaseNetworkEx", it) }
			ResourceRemote.Error(message = e.localizedMessage)
		}
	}

}