package com.example.uangku.feature.category.data

import com.example.uangku.feature.category.domain.Category

fun CategoryEntity.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        type = type,
    )
}

fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = id ?: 0,
        name = name,
        type = type
    )
}