package com.lcarlosfb.amarillolimon.ui.main.cart

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.lcarlosfb.amarillolimon.R
import com.lcarlosfb.amarillolimon.data.model.Cart
import com.lcarlosfb.amarillolimon.data.model.Product
import com.lcarlosfb.amarillolimon.databinding.CardViewCartBinding

class CartAdapter(
	private val cartList: ArrayList<Cart>,
	private val onItemLongClicked: (Cart) ->Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>(){


	//Para mostrar lista de item
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_cart,parent,false)
		return CartViewHolder(view)
	}
	//Recorre la lista de Category, detecta click del item en cierta posicion
	override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
		val cart = cartList[position]
		holder.bind(cart)
		holder.itemView.setOnLongClickListener { onItemLongClicked(cart)
			true}
	}
	//Cuenta cuantos item hay para repetir el proceso
	override fun getItemCount(): Int = cartList.size

	@SuppressLint("NotifyDataSetChanged")
	fun appendItems(newList : ArrayList<Cart>){
		cartList.clear()
		cartList.addAll(newList)
		notifyDataSetChanged()
	}

	class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

		private val binding = CardViewCartBinding.bind(itemView)

		fun bind(cart: Cart){
				//ponemos la info
			with(binding){
				tvCodigo.text = cart.id
				tvShortDescription.text= cart.shortDescription
				tvPrice.text= cart.price
				//Aqui falta la imagen y edt cantidad?

				val imageName = cart.id.toString()// Nombre de la imagen que deseas obtener la URL
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
