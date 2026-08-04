package com.example.uangku.feature.transaction.presentation

import com.example.uangku.feature.category.domain.Category
import com.example.uangku.core.domain.Type
import java.util.Date

sealed interface TransactionEvent {

    data object onScreenOpen : TransactionEvent

    data object onAddClick : TransactionEvent

    data class onEditClick(
        val category: Category
    ): TransactionEvent

    data class onDeleteClick(
        val category: Category
    ): TransactionEvent

    data class onTypeChange(
        val value: Type
    ) : TransactionEvent

    data object onDeleteConfirm : TransactionEvent

}