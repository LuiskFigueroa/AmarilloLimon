package com.lcarlosfb.amarillolimon.ui.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lcarlosfb.amarillolimon.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

	private lateinit var homeBinding: FragmentHomeBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		homeBinding = FragmentHomeBinding.inflate(inflater,container,false)
		val view = homeBinding.root
		return view
	}


}