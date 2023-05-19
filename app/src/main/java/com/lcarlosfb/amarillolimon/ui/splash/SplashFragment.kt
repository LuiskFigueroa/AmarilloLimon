package com.lcarlosfb.amarillolimon.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.lcarlosfb.amarillolimon.R
import com.lcarlosfb.amarillolimon.databinding.FragmentSplashBinding
import com.lcarlosfb.amarillolimon.ui.main.NavigationDrawerActivity

class SplashFragment : Fragment() {

	private lateinit var splashBinding : FragmentSplashBinding
	private lateinit var splashViewModel: SplashViewModel

	private var isSessionActive = false

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		splashBinding = FragmentSplashBinding.inflate(inflater, container, false)
		splashViewModel = ViewModelProvider(this)[SplashViewModel::class.java]
		val view = splashBinding.root

		splashViewModel.validateSessionActive()

		splashViewModel.isSessionActive.observe(viewLifecycleOwner){_isSessionActive->
			this.isSessionActive = _isSessionActive
		}

		Handler(Looper.myLooper()!!).postDelayed ({
			if(isSessionActive){
				val intent = Intent(activity, NavigationDrawerActivity::class.java)
				startActivity(intent)
				activity?.finish()
			}else{
				findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment(),
					NavOptions.Builder()
						.setPopUpTo(R.id.splashFragment, true)
						.build())
			}
		}, 2000)

		return  view
	}
}