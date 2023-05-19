package com.lcarlosfb.amarillolimon.ui.main.category

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lcarlosfb.amarillolimon.R
import com.lcarlosfb.amarillolimon.databinding.FragmentCategoryBinding

class CategoryFragment : Fragment() {

	private lateinit var categoryBinding: FragmentCategoryBinding
	private lateinit var categoryViewModel: CategoryViewModel

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		categoryBinding = FragmentCategoryBinding.inflate(inflater,container,false)
		categoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
		val view = categoryBinding.root


		return view
	}
}