package com.example.uangku.feature.category.presentation

import com.example.uangku.feature.category.domain.Category
import com.example.uangku.core.domain.Type

sealed interface CategoryEvent {

    data object onScreenOpen : CategoryEvent

    data object onAddClick : CategoryEvent

    data class onEditClick(
        val category: Category
    ): CategoryEvent

    data class onDeleteClick(
        val category: Category
    ): CategoryEvent

    data object onDismissFormDialog : CategoryEvent

    data object onDismissDeleteDialog : CategoryEvent

    data class onNameChange(
        val value: String
    ) : CategoryEvent

    data class onIconChange(
        val value: String
    ) : CategoryEvent

    data class onTypeChange(
        val value: Type
    ) : CategoryEvent

    data object onSave : CategoryEvent

    data object onDeleteConfirm : CategoryEvent

}