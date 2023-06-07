package com.lcarlosfb.amarillolimon.ui.main.detail

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.lcarlosfb.amarillolimon.R
import com.lcarlosfb.amarillolimon.data.model.Cart
import com.lcarlosfb.amarillolimon.data.model.Favorites
import com.lcarlosfb.amarillolimon.databinding.FragmentDetailBinding
import com.lcarlosfb.amarillolimon.ui.main.cart.CartAdapter
import com.lcarlosfb.amarillolimon.ui.main.category.CategoryFragmentArgs
import com.lcarlosfb.amarillolimon.ui.main.favorites.FavoriteAdapter
import com.lcarlosfb.amarillolimon.ui.main.favorites.FavoriteViewModel

class DetailFragment : Fragment() {

	private lateinit var detailBinding: FragmentDetailBinding
	private lateinit var detailViewModel: DetailViewModel
	private val args: DetailFragmentArgs by navArgs()

	private var isItemFavorite= false


	@SuppressLint("SetTextI18n")
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		detailBinding = FragmentDetailBinding.inflate(inflater,container,false)
		detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]
		val view = detailBinding.root


		detailViewModel.searchFavorite(args.product.id)


		detailBinding.tvDescription.text=args.product.description
		detailBinding.tvPrice.text = "$ "+ args.product.price
		detailBinding.tvShortCodigo.text = args.product.shortDescription + " " +args.product.id


		val imageName = args.product.id.toString() // Nombre de la imagen que deseas obtener la URL
		val storageRef = FirebaseStorage.getInstance().reference.child("images/$imageName")

		storageRef.downloadUrl.addOnSuccessListener { uri ->
			val imageUrl = uri.toString()
			// Aquí puedes usar la URL de la imagen como desees
			Log.d("FirebaseStorage", "URL de la imagen: $imageUrl")
			Glide.with(view).asBitmap().load(imageUrl).into(detailBinding.imvProduct)

		}.addOnFailureListener { exception ->
			// Manejar el error al obtener la URL de descarga de Firebase Storage
			Log.e("FirebaseStorage", "Error al obtener la URL de la imagen: ${exception.message}")
		}


		detailViewModel.errorMsg.observe(viewLifecycleOwner){errorMsg->
			Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
		}

		detailViewModel.productAddCartSuccess.observe(viewLifecycleOwner){
			findNavController().popBackStack()
		}


		detailViewModel.flagItemFavorite.observe(viewLifecycleOwner){flagItemFavorite->
			this.isItemFavorite = flagItemFavorite
			if (flagItemFavorite == true){
				detailBinding.imvFavorite.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite))
			}else{
				detailBinding.imvFavorite.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite_border))
			}
		}


		detailBinding.btnAAdir.setOnClickListener {
			detailViewModel.addToCart(args.product)
		}

		detailBinding.imvFavorite.setOnClickListener {
			if (isItemFavorite)
				Toast.makeText(context,"${args.product.id} ya está en tus favoritos", Toast.LENGTH_SHORT).show()
			else {
				isItemFavorite = true
				detailBinding.imvFavorite.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite))
				detailViewModel.saveFavorite(args.product)
			}
		}

		return view
	}



}