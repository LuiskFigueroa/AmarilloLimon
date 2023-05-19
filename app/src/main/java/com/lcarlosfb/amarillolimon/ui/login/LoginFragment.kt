package com.lcarlosfb.amarillolimon.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.lcarlosfb.amarillolimon.databinding.FragmentLoginBinding
import com.lcarlosfb.amarillolimon.ui.main.NavigationDrawerActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginFragment : Fragment() {

	private lateinit var loginBinding: FragmentLoginBinding
	private lateinit var loginViewModel: LoginViewModel

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		loginBinding = FragmentLoginBinding.inflate(inflater,container,false)
		loginViewModel = ViewModelProvider(this )[LoginViewModel::class.java]
		val view = loginBinding.root

		loginViewModel.errorMsg.observe(viewLifecycleOwner){ errorMsg ->
			GlobalScope.launch(Dispatchers.Main) {
				loginBinding.progressBar.visibility = View.GONE
			}
			Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
		}


		loginViewModel.isSuccessSignIn.observe(viewLifecycleOwner){
			GlobalScope.launch(Dispatchers.Main) {
				loginBinding.progressBar.visibility = View.GONE
			}
			val intent = Intent(activity, NavigationDrawerActivity::class.java)
			startActivity(intent)
			activity?.finish()
		}


		loginBinding.btnLoginIngresar.setOnClickListener {
			//throw RuntimeException("Test Crash")
			val email = loginBinding.edtLoginCorreo.text.toString()
			val password = loginBinding.edtLoginContraseA.text.toString()
			loginViewModel.validateFields(email,password)
			loginBinding.progressBar.visibility = View.VISIBLE
		}

		loginBinding.tvCrearCuenta.setOnClickListener {
			findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterAccountFragment())
		}


		return view
	}
}
