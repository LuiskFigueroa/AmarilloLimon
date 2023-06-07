package com.lcarlosfb.amarillolimon.ui.main.category

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.lcarlosfb.amarillolimon.R
import com.lcarlosfb.amarillolimon.data.model.Product
import com.lcarlosfb.amarillolimon.databinding.CardViewProductItemBinding
import java.io.File
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class NewProductAdapter (

	private val productList:ArrayList<Product>,
	private val onItemClicked: (Product) ->Unit,
	private val onItemLongClicked: (Product) ->Unit
) : RecyclerView.Adapter<NewProductAdapter.NewProductViewHolder>(){

	//Para mostrar lista de item
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewProductViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_product_item,parent,false)
		return NewProductViewHolder(view)
	}
	//Recorre la lista de Category, detecta click del item en cierta posicion
	override fun onBindViewHolder(holder: NewProductViewHolder, position: Int) {
		val product = productList[position]
		holder.bind(product)
		holder.itemView.setOnClickListener { onItemClicked(product) }
		holder.itemView.setOnLongClickListener { onItemLongClicked(product)
			true}
	}
	//Cuenta cuantos item hay para repetir el proceso
	override fun getItemCount(): Int = productList.size

	@SuppressLint("NotifyDataSetChanged")
	fun appendItems(newList : ArrayList<Product>){
		productList.clear()
		productList.addAll(newList)
		notifyDataSetChanged()
	}

	class NewProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

		private val binding = CardViewProductItemBinding.bind(itemView)

		@SuppressLint("SetTextI18n")
		fun bind(product : Product){
			with(binding){
				//ponemos la info
				tvCodigo.text = "Cod: " + product.id
				tvShortDescription.text= product.shortDescription
				tvPrice.text = "$ " + product.price
				//Aqui falta la imagen


				val imageName = product.id.toString()// Nombre de la imagen que deseas obtener la URL
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
