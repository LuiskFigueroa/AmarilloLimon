package com.lcarlosfb.amarillolimon.ui.main.home

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.lcarlosfb.amarillolimon.R
import com.lcarlosfb.amarillolimon.data.model.Category
import com.lcarlosfb.amarillolimon.databinding.CardViewCategoryItemBinding

class NewCategoryAdapter (
	private val categoryList:ArrayList<Category>,
	private val onItemClicked: (Category) ->Unit,
	private val onItemLongClicked: (Category) ->Unit
	) : RecyclerView.Adapter<NewCategoryAdapter.NewCategoryViewHolder>(){

//Para mostrar lista de item
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewCategoryViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_category_item,parent,false)
		return NewCategoryViewHolder(view)
	}
//Recorre la lista de Category, detecta click del item en cierta posicion
	override fun onBindViewHolder(holder: NewCategoryViewHolder, position: Int) {
		val category = categoryList[position]
		holder.bind(category)
		holder.itemView.setOnClickListener { onItemClicked(category) }
		holder.itemView.setOnLongClickListener { onItemLongClicked(category)
			true}
	}
//Cuenta cuantos item hay para repetir el proceso
	override fun getItemCount(): Int = categoryList.size

	@SuppressLint("NotifyDataSetChanged")
	fun appendItems(newList : ArrayList<Category>){
		categoryList.clear()
		categoryList.addAll(newList)
		notifyDataSetChanged()
	}

	class NewCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

		private val binding = CardViewCategoryItemBinding.bind(itemView)

		fun bind(category : Category){
			with(binding){
				//ponemos la info
				tvCategory.text = category.name
				//Aqui falta la imagen

				val imageName = category.name.toString()// Nombre de la imagen que deseas obtener la URL
				val storageRef = FirebaseStorage.getInstance().reference.child("images/$imageName")

				storageRef.downloadUrl.addOnSuccessListener { uri ->
					val imageUrl = uri.toString()
					// Aquí puedes usar la URL de la imagen como desees
					Log.d("FirebaseStorage", "URL de la imagen: $imageUrl")
					Glide.with(itemView).asBitmap().load(imageUrl).into(imvcategory)

				}.addOnFailureListener { exception ->
					// Manejar el error al obtener la URL de descarga de Firebase Storage
					Log.e("FirebaseStorage", "Error al obtener la URL de la imagen: ${exception.message}")
				}

			}
		}
	}
}
