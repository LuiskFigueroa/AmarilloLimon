package com.lcarlosfb.amarillolimon.ui.main.category

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.storage.FirebaseStorage
import com.lcarlosfb.amarillolimon.R
import com.lcarlosfb.amarillolimon.data.model.Product
import com.lcarlosfb.amarillolimon.databinding.FragmentCategoryBinding

class CategoryFragment : Fragment() {

	private lateinit var categoryBinding: FragmentCategoryBinding
	private lateinit var categoryViewModel: CategoryViewModel
	private lateinit var productAdapter: NewProductAdapter
	private var productList : ArrayList<Product> = ArrayList()


	private val args: CategoryFragmentArgs by navArgs()


	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		categoryBinding = FragmentCategoryBinding.inflate(inflater,container,false)
		categoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
		val view = categoryBinding.root

		val id = args.id

		categoryViewModel.sessionAdmin.observe(viewLifecycleOwner){sessionAdmin->
			categoryBinding.fbtnNewProduct.visibility = View.VISIBLE
		}

		categoryViewModel.productList.observe(viewLifecycleOwner){productList->
			productAdapter.appendItems(productList)
		}

		categoryBinding.fbtnNewProduct.setOnClickListener {
			findNavController().navigate(CategoryFragmentDirections.actionCategoryFragmentToNewProductFragment(id))
		}

		productAdapter = NewProductAdapter(productList,
			onItemClicked = { product -> onItemClicked(product) },
			onItemLongClicked = {product ->	deleteProduct(product)
			}
		)
		categoryBinding.recyclerViewProduct.adapter = productAdapter

		categoryViewModel.validateAdmin()
		categoryViewModel.loadCategory(id)

		return view
	}

	private fun deleteProduct(product: Product) {

		val alertDialog: AlertDialog? = activity?.let {
			val builder= AlertDialog.Builder(it)

			builder.apply {
				setMessage("¿Desea eliminar el producto: ${product.id}?")
				setPositiveButton(R.string.accept){ dialog, id->
					val idCategory = args.id
					categoryViewModel.deleteCategory(product,idCategory)

					val imageName = product.id.toString()// Nombre de la imagen que deseas obtener la URL
					val storageRef = FirebaseStorage.getInstance().reference.child("images/$imageName")
					storageRef.delete()
					//favoritesViewModel.deleteFavoriteMovie(localMovie)
					//favoritesViewModel.loadFavoritesMovies()
				}
				setNegativeButton(R.string.cancel){dialog, id->
				}
			}
			builder.create()
		}
		alertDialog?.show()
	}

	private fun onItemClicked(product: Product) {
		val action = CategoryFragmentDirections.actionCategoryFragmentToDetailFragment(product)
		findNavController().navigate(action)
	}
}