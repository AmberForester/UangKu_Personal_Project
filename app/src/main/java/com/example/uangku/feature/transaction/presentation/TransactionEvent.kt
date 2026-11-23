package com.example.uangku.feature.transaction.presentation

import com.example.uangku.core.domain.Type
import com.example.uangku.feature.category.domain.Category

sealed interface TransactionEvent {

    data object onScreenOpen : TransactionEvent

    data object onAddClick : TransactionEvent

    data object onPreviousMonth: TransactionEvent

    data object onNextMonth: TransactionEvent

    data class onSearchQueryChange(val query: String) : TransactionEvent

    data object onClearSearch : TransactionEvent

    data class onFilterSelect(val type: Type?, val category: Category?) : TransactionEvent

    data class onCategoryFilterSelect(val category: Category?) : TransactionEvent

    data object onClearTypeFilter : TransactionEvent

    data object onClearCategoryFilter : TransactionEvent

    data object onOpenFilterSheet : TransactionEvent

    data object onDismissFilterSheet : TransactionEvent

    data object onResetFilter : TransactionEvent

}
