package com.lcarlosfb.amarillolimon.data

import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lcarlosfb.amarillolimon.data.model.Favorites
import kotlinx.coroutines.tasks.await

class FavoritesRepository {
	private var db = Firebase.firestore

	suspend fun saveItemFavorites(favorites: Favorites, uid: String): ResourceRemote<String?> {
		return try{
			favorites.id?.let { db.collection("users").document(uid)
				.collection("favoritos").document(it).set(favorites).await() }
			ResourceRemote.Success(data = favorites.id)
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

	suspend fun loadFavoritesItems(uid: String): ResourceRemote<QuerySnapshot?>{
		return try{
			//obtengo cierto usuario
			val result = db.collection("users").document(uid)
				.collection("favoritos").get().await()
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

	suspend fun deleteFavoriteItem(favorites: Favorites, uid: String): ResourceRemote<QuerySnapshot?>{
		return try{
			//obtengo cierto usuario
			favorites.id?.let { db.collection("users").document(uid)
				.collection("favoritos").document(it).delete().await() }
			val result = db.collection("users").document(uid)
				.collection("favoritos").get().await()
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

	suspend fun searchFavoriteItems(uid: String): ResourceRemote<QuerySnapshot?>{
		return try{
			//obtengo cierto usuario
			val result = db.collection("users").document(uid)
				.collection("favoritos").get().await()
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


}
