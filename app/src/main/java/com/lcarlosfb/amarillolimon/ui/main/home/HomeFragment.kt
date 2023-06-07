package com.lcarlosfb.amarillolimon.ui.main.home

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.storage.FirebaseStorage
import com.lcarlosfb.amarillolimon.R
import com.lcarlosfb.amarillolimon.data.model.Category
import com.lcarlosfb.amarillolimon.data.model.Product
import com.lcarlosfb.amarillolimon.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

	private lateinit var homeBinding : FragmentHomeBinding
	private lateinit var homeViewModel : HomeViewModel
	private lateinit var categoryAdapter : NewCategoryAdapter
	//private var categoryList : ArrayList<Category> = ArrayList()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		homeBinding = FragmentHomeBinding.inflate(inflater,container,false)
		homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
		val view = homeBinding.root

		homeViewModel.sessionAdmin.observe(viewLifecycleOwner){sessionAdmin->
			homeBinding.fbtnNewCategory.visibility = View.VISIBLE
		}

		homeViewModel.errorMsg.observe(viewLifecycleOwner){errorMsg->
			Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
		}

		homeViewModel.categoryList.observe(viewLifecycleOwner){categoryList->
			categoryAdapter.appendItems(categoryList)
		}

		homeBinding.fbtnNewCategory.setOnClickListener {
			findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToNewCategoryFragment())
		}

		homeViewModel.validateAdmin()

		//configuramos recyclerview y adapter
		val categoryList= ArrayList<Category>()
		categoryAdapter = NewCategoryAdapter(categoryList,
			onItemClicked = { category -> onItemClicked(category) },
			onItemLongClicked = {category ->deleteCategory(category)}
			)

		homeBinding.recyclerViewCategory.apply {
			layoutManager = LinearLayoutManager(this@HomeFragment.requireContext())
			adapter = categoryAdapter
			setHasFixedSize(false)
		}

		homeViewModel.loadCategory()

		return view
	}

	private fun onItemClicked(category: Category) {
		val action = HomeFragmentDirections.actionHomeFragmentToCategoryFragment(category.id.toString())
		findNavController().navigate(action)
	}

	private fun deleteCategory(category: Category) {
		val alertDialog: AlertDialog? = activity?.let {
			val builder= AlertDialog.Builder(it)
			builder.apply {
				setMessage("¿Desea eliminar la categoria de: ${category.name}?")
				setPositiveButton(R.string.accept){ dialog, id->
					homeViewModel.deleteCategory(category)

					val imageName = category.id.toString()// Nombre de la imagen que deseas obtener la URL
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

}
