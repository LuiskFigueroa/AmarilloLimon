package com.lcarlosfb.amarillolimon.ui.main.cart

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
import com.lcarlosfb.amarillolimon.databinding.FragmentCartBinding


class CartFragment : Fragment() {

	private lateinit var cartBinding: FragmentCartBinding
	private lateinit var cartViewModel: CartViewModel
	private lateinit var cartAdapter: CartAdapter


	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		cartBinding = FragmentCartBinding.inflate(inflater,container,false)
		cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
		val view = cartBinding.root

		/*val cantidad = resources.getStringArray(R.array.cantidad)
		val adapter = ArrayAdapter(requireContext(),R.layout.list_cantidad,cantidad)
		cartBinding.edtCantidad.setAdapter(adapter) */

		cartViewModel.cartList.observe(viewLifecycleOwner){cartList->
			cartAdapter.appendItems(cartList)
		}

		cartViewModel.errorMsg.observe(viewLifecycleOwner){errorMsg->
			Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
		}






		//configuramos recyclerview y adapter
		val cartList= ArrayList<Cart>()
		//val cartAdapter = CartAdapter(cartList)
		this.cartAdapter = CartAdapter(cartList,
			onItemLongClicked = {cart ->deleteItem(cart)},

			)

		cartBinding.recyclerViewCart.apply {
			layoutManager = LinearLayoutManager(this@CartFragment.requireContext())
			adapter = cartAdapter
			setHasFixedSize(false)
		}

		cartViewModel.loadCart()


		return view
	}

	private fun deleteItem(cart: Cart) {
		val alertDialog: AlertDialog? = activity?.let {
			val builder= AlertDialog.Builder(it)
			builder.apply {
				setMessage("¿Deseas eliminar el producto: ${cart.id}, del carrito de compras?")
				setPositiveButton(R.string.accept){ dialog, id->
					cartViewModel.deleteCartItem(cart)
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
