package com.example.uangku.feature.transaction.presentation

import com.example.uangku.core.domain.Type
import com.example.uangku.feature.category.domain.Category
import java.util.Date

sealed interface TransactionFormEvent {

    data class OnScreenOpen(
        val id: Long
    ): TransactionFormEvent

    data class OnDateChange(
        val date: Date
    ): TransactionFormEvent

    data class OnTypeChange(
        val type: Type
    ): TransactionFormEvent

    data class OnDescriptionChange(
        val desc: String
    ): TransactionFormEvent

    data class OnAmountChange(
        val amount: String
    ): TransactionFormEvent

    data class OnCategoryChange(
        val category: Category
    ): TransactionFormEvent

    data object OnSave: TransactionFormEvent

}