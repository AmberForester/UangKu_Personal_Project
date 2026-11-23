package com.example.uangku.feature.analysis.presentation

import com.example.uangku.core.domain.Type
import com.example.uangku.feature.analysis.domain.CategorySpending

sealed interface AnalysisEvent {
    data object OnNextMonth : AnalysisEvent
    data object OnPreviousMonth : AnalysisEvent
    data class OnSelectCategory(val category: CategorySpending?) : AnalysisEvent
    data class OnSelectType(val type: Type) : AnalysisEvent
    data class OnLoadCategoryDetail(val categoryId: Int, val historyMonths: Int = 6) : AnalysisEvent
    data class OnChangeCategoryDetailHistoryMonths(val months: Int) : AnalysisEvent
}
