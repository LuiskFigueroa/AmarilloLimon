package com.lcarlosfb.amarillolimon.ui.main.category.newproduct


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.storage.FirebaseStorage

import com.lcarlosfb.amarillolimon.databinding.FragmentNewProductBinding
import com.lcarlosfb.amarillolimon.ui.main.category.CategoryFragmentArgs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NewProductFragment : Fragment() {

	private lateinit var newProductBinding: FragmentNewProductBinding
	private lateinit var newProductViewModel: NewProductViewModel

	lateinit var ImageUri : Uri

	private val args: CategoryFragmentArgs by navArgs()


	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		newProductBinding = FragmentNewProductBinding.inflate(inflater,container,false)
		newProductViewModel = ViewModelProvider(this)[NewProductViewModel::class.java]
		val view = newProductBinding.root

		val idCategory = args.id

		newProductViewModel.errorMsg.observe(viewLifecycleOwner){errorMsg->
			Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
		}

		newProductViewModel.createProductSuccess.observe(viewLifecycleOwner){
			findNavController().popBackStack()
		}


		newProductBinding.imvNewProduct.setOnClickListener {
			selectImage()
		}



		newProductBinding.btnCreateNewProduct.setOnClickListener {
			val id = newProductBinding.edtId.text.toString()
			val shortDescription = newProductBinding.edtShortDescription.text.toString()
			val description = newProductBinding.edtDescription.text.toString()
			val price = newProductBinding.edtPrice.text.toString()
			//val uri = newProductBinding.imvNewProduct.toString()
			uploadImage(id)
			newProductViewModel.createProduct(id, shortDescription, description, price, idCategory)

		}



		return  view
	}

	private fun selectImage() {
		val intent = Intent()
		intent.type = "image/*"
		intent.action = Intent.ACTION_GET_CONTENT

		startActivityForResult(intent, 100)
	}


	private fun uploadImage(id: String) {
		GlobalScope.launch {
			if (isAdded) {
				try {
					val context = requireContext()
					val storageReference = FirebaseStorage.getInstance().getReference("images/$id")

					storageReference.putFile(ImageUri).addOnSuccessListener {
						newProductBinding.imvNewProduct.setImageURI(null)
						Toast.makeText(context, "Imagen cargada", Toast.LENGTH_SHORT).show()
					}.addOnFailureListener {
						Toast.makeText(context, "Falló la carga", Toast.LENGTH_SHORT).show()
					}
				} catch (e: IllegalStateException) {
					Log.e("NewProductFragment", "Fragment not attached to a context")
				}
			}
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (requestCode == 100 && resultCode == RESULT_OK){
			ImageUri = data?.data!!
			newProductBinding.imvNewProduct.setImageURI(ImageUri)
		}

	}

}
