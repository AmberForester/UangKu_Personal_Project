package com.example.uangku.feature.category.data

import com.example.uangku.core.domain.Type

object DefaultCategories {

    val categories = listOf(

        CategoryEntity(
            name = "Makanan",
            type = Type.EXPENSE
        ),
        CategoryEntity(
            name = "Transportasi",
            type = Type.EXPENSE
        ),
        CategoryEntity(
            name = "Belanja",
            type = Type.EXPENSE
        ),
        CategoryEntity(
            name = "Hiburan",
            type = Type.EXPENSE
        ),
        CategoryEntity(
            name = "Lainnya",
            type = Type.EXPENSE
        ),
        CategoryEntity(
            name = "Gaji",
            type = Type.INCOME
        ),
        CategoryEntity(
            name = "Bonus",
            type = Type.INCOME
        ),
        CategoryEntity(
            name = "Lainnya",
            type = Type.INCOME
        )
    )

}