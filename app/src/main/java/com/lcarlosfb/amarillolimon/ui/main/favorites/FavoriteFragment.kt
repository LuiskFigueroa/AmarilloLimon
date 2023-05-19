package com.lcarlosfb.amarillolimon.ui.main.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.lcarlosfb.amarillolimon.R
import com.lcarlosfb.amarillolimon.databinding.FragmentFavoriteBinding


class FavoriteFragment : Fragment() {

	private lateinit var favoriteBinding: FragmentFavoriteBinding
	private lateinit var favoriteViewModel: FavoriteViewModel

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		favoriteBinding = FragmentFavoriteBinding.inflate(inflater,container,false)
		favoriteViewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]
		val view = favoriteBinding.root


		return view
	}

}