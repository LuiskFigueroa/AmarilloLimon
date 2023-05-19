package com.lcarlosfb.amarillolimon.ui.main.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lcarlosfb.amarillolimon.R
import com.lcarlosfb.amarillolimon.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

	private lateinit var detailBinding: FragmentDetailBinding
	private lateinit var detailViewModel: DetailViewModel

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		detailBinding = FragmentDetailBinding.inflate(inflater,container,false)
		detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]
		val view = detailBinding.root


		return view
	}

}