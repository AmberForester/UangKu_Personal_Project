package com.example.uangku.feature.analysis.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.uangku.core.domain.Type
import com.example.uangku.feature.analysis.domain.AnalysisOverview
import com.example.uangku.feature.analysis.domain.CategoryAnalysisDetail
import com.example.uangku.feature.analysis.domain.CategorySpending
import com.example.uangku.feature.period.domain.FinancialPeriod
import java.time.YearMonth

enum class CategoryDetailSortOrder {
    NEWEST,
    HIGHEST_AMOUNT,
    LOWEST_AMOUNT
}

@RequiresApi(Build.VERSION_CODES.O)
data class AnalysisState(
    val selectedMonth: YearMonth = YearMonth.now(),
    val selectedType: Type = Type.EXPENSE,
    val financialPeriod: FinancialPeriod? = null,
    val overview: AnalysisOverview = AnalysisOverview(),
    val selectedCategory: CategorySpending? = null,
    val categoryDetail: CategoryAnalysisDetail? = null,
    val categoryDetailHistoryMonths: Int = 6,
    val categoryDetailSortOrder: CategoryDetailSortOrder = CategoryDetailSortOrder.NEWEST,
    val isLoadingCategoryDetail: Boolean = false,
    val isLoading: Boolean = false
)
