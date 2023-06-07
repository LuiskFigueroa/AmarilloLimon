package com.lcarlosfb.amarillolimon.ui.main.home.newcategory

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.storage.FirebaseStorage
import com.lcarlosfb.amarillolimon.databinding.FragmentNewCategoryBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NewCategoryFragment : Fragment() {

	private lateinit var newCategoryBinding: FragmentNewCategoryBinding
	private lateinit var newCategoryViewModel: NewCategoryViewModel

	lateinit var ImageUri : Uri

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		newCategoryBinding = FragmentNewCategoryBinding.inflate(inflater,container,false)
		newCategoryViewModel = ViewModelProvider(this)[NewCategoryViewModel::class.java]
		val view = newCategoryBinding.root

		newCategoryViewModel.errorMsg.observe(viewLifecycleOwner){errorMsg->
			Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
		}

		newCategoryViewModel.createCategorySuccess.observe(viewLifecycleOwner){createCategorySuccess->
			findNavController().popBackStack()
		}

		newCategoryBinding.btnCreateNewCategory.setOnClickListener {
			val name = newCategoryBinding.edtNameCategory.text.toString()
			//Falta enviar la imagen

			uploadImage(name)
			newCategoryViewModel.createCategory(name)
		}

		newCategoryBinding.imvNewcategory.setOnClickListener {
			selectImage()
		}


		return view
	}

	private fun selectImage() {
		val intent = Intent()
		intent.type = "image/*"
		intent.action = Intent.ACTION_GET_CONTENT

		startActivityForResult(intent, 100)
	}

	private fun uploadImage(name: String) {
		GlobalScope.launch {
			if (isAdded) {
				try {
					val context = requireContext()
					val storageReference = FirebaseStorage.getInstance().getReference("images/$name")

					storageReference.putFile(ImageUri).addOnSuccessListener {
						newCategoryBinding.imvNewcategory.setImageURI(null)
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
		if (requestCode == 100 && resultCode == Activity.RESULT_OK){
			ImageUri = data?.data!!
			newCategoryBinding.imvNewcategory.setImageURI(ImageUri)
		}

	}

}
