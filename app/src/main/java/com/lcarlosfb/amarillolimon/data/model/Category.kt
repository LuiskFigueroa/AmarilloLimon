package com.lcarlosfb.amarillolimon.data.model

import java.io.Serializable

data class Category(
	var id: String? = null,
	var name: String? = null,
	var picture: String? = null,
) : Serializable
