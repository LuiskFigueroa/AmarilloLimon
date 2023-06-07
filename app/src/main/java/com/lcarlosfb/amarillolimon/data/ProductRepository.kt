package com.lcarlosfb.amarillolimon.data

import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lcarlosfb.amarillolimon.data.model.Product
import kotlinx.coroutines.tasks.await

class ProductRepository {

	private var db = Firebase.firestore

	suspend fun loadProduct(id :String): ResourceRemote<QuerySnapshot?>{
		return try{
			//obtengo cierto usuario
			val result = db.collection("categorias").document(id)
				.collection("codigo").get().await()
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

	suspend fun saveProduct(product: Product, idCategory: String): ResourceRemote<String?> {
		return try{
			product.id?.let { db.collection("categorias").document(idCategory)
				.collection("codigo").document(it).set(product).await() }
			ResourceRemote.Success(data = product.id)
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

	suspend fun deleteCategory(product: Product, idCategory: String): ResourceRemote<QuerySnapshot?> {
		return try{
			//obtengo cierto usuario
			product.id?.let { db.collection("categorias").document(idCategory)
				.collection("codigo").document(it).delete().await()}
			val result = db.collection("categorias").document(idCategory)
				.collection("codigo").get().await()
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
