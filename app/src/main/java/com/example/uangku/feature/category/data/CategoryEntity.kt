package com.example.uangku.feature.category.data
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.uangku.core.domain.Type

@Entity(tableName = "categories")
data class CategoryEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    val type: Type

){}
