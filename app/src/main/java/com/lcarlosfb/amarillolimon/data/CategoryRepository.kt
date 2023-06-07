package com.lcarlosfb.amarillolimon.data

import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lcarlosfb.amarillolimon.data.model.Category
import kotlinx.coroutines.tasks.await

class CategoryRepository {

	private var db = Firebase.firestore

	suspend fun saveCategory(category: Category): ResourceRemote<String?> {
		return try{
			//el codigo comentado es para traer id generado por firebase
			//val path = db.collection("categorias")
			//val documentCategory = path.document()
			//category.id = documentCategory.id
			//category.id?.let { path.document(it).set(category).await() }
			category.id?.let { db.collection("categorias").document(it).set(category).await() }
			ResourceRemote.Success(data = category.id)
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

	suspend fun loadCategory(): ResourceRemote<QuerySnapshot?>{
		return try{
			//obtengo cierto usuario
			val result = db.collection("categorias").get().await()
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

	suspend fun deleteCategory(category: Category): ResourceRemote<QuerySnapshot?>{
		return try{
			//obtengo cierto usuario
			category.id?.let { db.collection("categorias").document(it).delete().await() }
			val result = db.collection("categorias").get().await()
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
