package com.lcarlosfb.amarillolimon.ui.main.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.lcarlosfb.amarillolimon.MainActivity
import com.lcarlosfb.amarillolimon.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

	private lateinit var profileBinding: FragmentProfileBinding
	private lateinit var profileViewModel: ProfileViewModel

	@SuppressLint("SetTextI18n")
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		profileBinding = FragmentProfileBinding.inflate(inflater,container,false)
		profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
		val view = profileBinding.root

		profileViewModel.loadUserInfo()

		profileViewModel.errorMsg.observe(viewLifecycleOwner){errorMsg ->
			Toast.makeText(requireActivity(),errorMsg,Toast.LENGTH_SHORT).show()
		}

		profileViewModel.userLoaded.observe(viewLifecycleOwner){user ->
			with(profileBinding){
				tvName.text = user?.name + " " + user?.lastName
				tvEmail.text = user?.email
			}
		}


		profileBinding.btnSignOut.setOnClickListener {
			profileViewModel.signOut()
			val intent = Intent(activity, MainActivity::class.java)
			startActivity(intent)
			activity?.finish()
		}

		return view
	}
}