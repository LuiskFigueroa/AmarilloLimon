package com.lcarlosfb.amarillolimon.ui.main.favorites

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.lcarlosfb.amarillolimon.R
import com.lcarlosfb.amarillolimon.data.model.Favorites
import com.lcarlosfb.amarillolimon.databinding.CardViewFavoritesBinding

class FavoriteAdapter (
	private val favoritesList:ArrayList<Favorites>,
	private val onItemLongClicked: (Favorites) ->Unit
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>(){

	//Para mostrar lista de item
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_favorites,parent,false)
		return FavoriteViewHolder(view)
	}
	//Recorre la lista de Category, detecta click del item en cierta posicion
	override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
		val favorite = favoritesList[position]
		holder.bind(favorite)
		holder.itemView.setOnLongClickListener { onItemLongClicked(favorite)
			true}
	}
	//Cuenta cuantos item hay para repetir el proceso
	override fun getItemCount(): Int = favoritesList.size

	@SuppressLint("NotifyDataSetChanged")
	fun appendItems(newList : ArrayList<Favorites>){
		favoritesList.clear()
		favoritesList.addAll(newList)
		notifyDataSetChanged()
	}

	class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

		private val binding = CardViewFavoritesBinding.bind(itemView)

		fun bind(favorites: Favorites){
			with(binding){
				//ponemos la info
				tvCodigo.text = favorites.id
				tvShortDescription.text= favorites.shortDescription
				tvPrice.text= favorites.price
				//Aqui falta la imagen y edt cantidad?

				val imageName = favorites.id.toString()// Nombre de la imagen que deseas obtener la URL
				val storageRef = FirebaseStorage.getInstance().reference.child("images/$imageName")

				storageRef.downloadUrl.addOnSuccessListener { uri ->
					val imageUrl = uri.toString()
					// Aquí puedes usar la URL de la imagen como desees
					Log.d("FirebaseStorage", "URL de la imagen: $imageUrl")
					Glide.with(itemView).asBitmap().load(imageUrl).into(imvPicture)

				}.addOnFailureListener { exception ->
					// Manejar el error al obtener la URL de descarga de Firebase Storage
					Log.e("FirebaseStorage", "Error al obtener la URL de la imagen: ${exception.message}")
				}


			}
		}
	}
}