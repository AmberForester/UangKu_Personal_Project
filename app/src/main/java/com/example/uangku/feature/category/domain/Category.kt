package com.example.uangku.feature.category.domain

import com.example.uangku.core.domain.Type

data class Category (
    val id: Long? = null,
    val name: String,
    val type: Type,
) {}