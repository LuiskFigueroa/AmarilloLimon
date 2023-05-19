package com.lcarlosfb.amarillolimon.ui.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.lcarlosfb.amarillolimon.R
import com.lcarlosfb.amarillolimon.databinding.FragmentRegisterAccountBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class RegisterAccountFragment : Fragment() {

	private lateinit var registerAccountBinding : FragmentRegisterAccountBinding
	private lateinit var registerAccountViewModel: RegisterAccountViewModel

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		registerAccountBinding = FragmentRegisterAccountBinding.inflate(inflater,container,false)
		registerAccountViewModel = ViewModelProvider(this)[RegisterAccountViewModel::class.java]
		val view = registerAccountBinding.root

		val genderList = resources.getStringArray(R.array.genders)
		val adapter = ArrayAdapter(requireContext(), R.layout.list_gender, genderList)
		registerAccountBinding.edtRegisterGender.setAdapter(adapter)


		val edtNameError = androidx.lifecycle.Observer<String>{ nameError ->
			registerAccountBinding.edtRegisterNames.setError(nameError)
		}
		registerAccountViewModel.edtNameError.observe(viewLifecycleOwner, edtNameError)

		val edtLastNameError = androidx.lifecycle.Observer<String>{ lastNameError ->
			registerAccountBinding.edtRegisterLastName.setError(lastNameError)
		}
		registerAccountViewModel.edtlastNameError.observe(viewLifecycleOwner, edtLastNameError)

		val edtEmailError = androidx.lifecycle.Observer<String>{ emailError ->
			registerAccountBinding.edtRegisterEmail.setError(emailError)
		}
		registerAccountViewModel.edtEmailError.observe(viewLifecycleOwner, edtEmailError)

		val edtPhoneError = androidx.lifecycle.Observer<String>{ phoneError ->
			registerAccountBinding.edtRegisterNumber.setError(phoneError)
		}
		registerAccountViewModel.edtPhoneError.observe(viewLifecycleOwner, edtPhoneError)

		val edtGenderError = androidx.lifecycle.Observer<String>{ genderError ->
			GlobalScope.launch(Dispatchers.Main) {
				registerAccountBinding.progressBar2.visibility = View.GONE
			}
			Toast.makeText(this.requireContext(), genderError, Toast.LENGTH_SHORT).show()
		}
		registerAccountViewModel.edtGenderError.observe(viewLifecycleOwner, edtGenderError)

		val edtDocumentError = androidx.lifecycle.Observer<String>{ docIdError ->
			registerAccountBinding.edtRegisterId.setError(docIdError)
		}
		registerAccountViewModel.edtDocIdError.observe(viewLifecycleOwner, edtDocumentError)

		val edtPasswordError = androidx.lifecycle.Observer<String>{ passwordError ->
			registerAccountBinding.edtRegisterPassword.setError(passwordError)
		}
		registerAccountViewModel.edtPasswordError.observe(viewLifecycleOwner, edtPasswordError)

		val edtrepPasswordError = androidx.lifecycle.Observer<String>{ repPasswordError ->
			registerAccountBinding.edtRegisterRepPassword.setError(repPasswordError)
		}
		registerAccountViewModel.edtrepPasswordError.observe(viewLifecycleOwner, edtrepPasswordError)

		val verificarPasswordObserver = androidx.lifecycle.Observer<String>{ verificarPasswordError->
			registerAccountBinding.edtRegisterRepPassword.setError(verificarPasswordError)
		}
		registerAccountViewModel.verificarPasswordError.observe(viewLifecycleOwner,verificarPasswordObserver)

		val toastCamposObserver = androidx.lifecycle.Observer<String?>{ toastError ->
			GlobalScope.launch(Dispatchers.Main) {
				registerAccountBinding.progressBar2.visibility = View.GONE
			}
			Toast.makeText(requireContext(), toastError, Toast.LENGTH_SHORT).show()
		}
		registerAccountViewModel.toastError.observe(viewLifecycleOwner, toastCamposObserver)


/*		registerAccountViewModel.errorMsg.observe(viewLifecycleOwner){
			GlobalScope.launch(Dispatchers.Main) {
				registerAccountBinding.progressBar2.visibility = View.GONE
			}
			Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
		} */


		registerAccountViewModel.isSuccessSignUp.observe(viewLifecycleOwner){
			GlobalScope.launch(Dispatchers.Main) {
				registerAccountBinding.progressBar2.visibility = View.GONE
			}
			findNavController().popBackStack()
		}

		registerAccountBinding.btnCreateAccount.setOnClickListener {
			val (name, lastName, email, phone, gender, docId, password, repPassword) = datosUsuario()
			registerAccountViewModel.registerPerson(
				name,
				lastName,
				email,
				phone,
				gender,
				docId,
				password,
				repPassword
				)
			registerAccountBinding.progressBar2.visibility = View.VISIBLE
		}



		return view
	}

	data class User (var name: String, var lastName: String, var email: String, var phone: String,
	                 var gender: String, var docId: String, var password: String, var repPassword: String)

	private fun datosUsuario(): User {
		val name = registerAccountBinding.edtRegisterNames.text.toString()
		val lastName = registerAccountBinding.edtRegisterLastName.text.toString()
		val email = registerAccountBinding.edtRegisterEmail.text.toString()
		val phone = registerAccountBinding.edtRegisterNumber.text.toString()
		val gender = registerAccountBinding.edtRegisterGender.text.toString()
		val docId = registerAccountBinding.edtRegisterId.text.toString()
		val password = registerAccountBinding.edtRegisterPassword.text.toString()
		val repPassword = registerAccountBinding.edtRegisterRepPassword.text.toString()
		return User(name,lastName,email,phone,gender,docId,password,repPassword)
	}
}
