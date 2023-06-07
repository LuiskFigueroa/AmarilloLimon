package com.lcarlosfb.amarillolimon.ui.main.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.storage.FirebaseStorage
import com.lcarlosfb.amarillolimon.MainActivity
import com.lcarlosfb.amarillolimon.databinding.FragmentProfileBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {

	private lateinit var profileBinding: FragmentProfileBinding
	private lateinit var profileViewModel: ProfileViewModel

	lateinit var ImageUri: Uri

	@SuppressLint("SetTextI18n")
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		profileBinding = FragmentProfileBinding.inflate(inflater, container, false)
		profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
		val view = profileBinding.root

		profileViewModel.loadUserInfo()

		profileViewModel.errorMsg.observe(viewLifecycleOwner) { errorMsg ->
			Toast.makeText(requireActivity(), errorMsg, Toast.LENGTH_SHORT).show()
		}

		profileViewModel.userLoaded.observe(viewLifecycleOwner) { user ->
			with(profileBinding) {
				tvName.text = user?.name + " " + user?.lastName
				tvEmail.text = user?.email
			}
			/*profileBinding.btnUpload.setOnClickListener {
				if (user != null) {
					uploadImage(user.uid.toString())
				}
			}
			profileBinding.btnDelete.setOnClickListener {
				if (user != null) {
					deleteImage(user.uid.toString())
				}
			}*/
		}

		/*profileBinding.btnSelect.setOnClickListener {
			selectImage()
		}*/




		profileBinding.btnSignOut.setOnClickListener {
			profileViewModel.signOut()
			val intent = Intent(activity, MainActivity::class.java)
			startActivity(intent)
			activity?.finish()
		}

		return view
	}
}

