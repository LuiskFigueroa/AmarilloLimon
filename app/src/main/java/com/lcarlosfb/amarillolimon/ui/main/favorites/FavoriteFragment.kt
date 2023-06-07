package com.lcarlosfb.amarillolimon.ui.main.favorites

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lcarlosfb.amarillolimon.R
import com.lcarlosfb.amarillolimon.data.model.Cart
import com.lcarlosfb.amarillolimon.data.model.Favorites
import com.lcarlosfb.amarillolimon.databinding.FragmentFavoriteBinding
import com.lcarlosfb.amarillolimon.ui.main.cart.CartAdapter


class FavoriteFragment : Fragment() {

	private lateinit var favoriteBinding: FragmentFavoriteBinding
	private lateinit var favoriteViewModel: FavoriteViewModel
	private lateinit var favoriteAdapter: FavoriteAdapter

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		favoriteBinding = FragmentFavoriteBinding.inflate(inflater,container,false)
		favoriteViewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]
		val view = favoriteBinding.root

		favoriteViewModel.favoriteList.observe(viewLifecycleOwner){favoriteList->
			favoriteAdapter.appendItems(favoriteList)
		}

		favoriteViewModel.errorMsg.observe(viewLifecycleOwner){errorMsg->
			Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
		}


		//configuramos recyclerview y adapter
		val favoritesList= ArrayList<Favorites>()
		favoriteAdapter = FavoriteAdapter(favoritesList,
			onItemLongClicked = {favorite ->deleteItem(favorite)}
		)

		favoriteBinding.favoriteRecyclerView.apply {
			layoutManager = LinearLayoutManager(this@FavoriteFragment.requireContext())
			adapter = favoriteAdapter
			setHasFixedSize(false)
		}

		favoriteViewModel.loadFavorites()


		return view
	}

	private fun deleteItem(favorite: Favorites) {
		val alertDialog: AlertDialog? = activity?.let {
			val builder= AlertDialog.Builder(it)
			builder.apply {
				setMessage("¿Deseas eliminar el producto: ${favorite.id}, de sus favoritos?")
				setPositiveButton(R.string.accept){ dialog, id->
					favoriteViewModel.deleteFavoriteItem(favorite)
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