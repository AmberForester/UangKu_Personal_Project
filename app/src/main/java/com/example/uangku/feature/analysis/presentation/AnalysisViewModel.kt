package com.example.uangku.feature.analysis.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uangku.feature.analysis.domain.AnalysisUseCase
import com.example.uangku.feature.period.domain.PeriodUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalCoroutinesApi::class)
class AnalysisViewModel(
    private val analysisUseCase: AnalysisUseCase,
    private val periodUseCase: PeriodUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AnalysisState())
    val state = _state.asStateFlow()

    private var categoryDetailJob: Job? = null

    init {
        initializeMonth()
        observePeriod()
        observeOverview()
    }

    fun onEvent(event: AnalysisEvent) {
        when (event) {
            AnalysisEvent.OnNextMonth -> {
                _state.update {
                    it.copy(
                        selectedMonth = it.selectedMonth.plusMonths(1),
                        selectedCategory = null
                    )
                }
            }
            AnalysisEvent.OnPreviousMonth -> {
                _state.update {
                    it.copy(
                        selectedMonth = it.selectedMonth.minusMonths(1),
                        selectedCategory = null
                    )
                }
            }
            is AnalysisEvent.OnSelectType -> {
                if (_state.value.selectedType != event.type) {
                    _state.update {
                        it.copy(
                            selectedType = event.type,
                            selectedCategory = null
                        )
                    }
                }
            }
            is AnalysisEvent.OnSelectCategory -> {
                _state.update {
                    val newSelection = if (it.selectedCategory?.categoryId == event.category?.categoryId) {
                        null
                    } else {
                        event.category
                    }
                    it.copy(selectedCategory = newSelection)
                }
            }
            is AnalysisEvent.OnLoadCategoryDetail -> {
                loadCategoryDetail(event.categoryId, event.historyMonths)
            }
            is AnalysisEvent.OnChangeCategoryDetailHistoryMonths -> {
                val currentCatId = _state.value.categoryDetail?.categoryId
                if (currentCatId != null && _state.value.categoryDetailHistoryMonths != event.months) {
                    loadCategoryDetail(currentCatId, event.months)
                }
            }
            is AnalysisEvent.OnChangeCategoryDetailSortOrder -> {
                _state.update {
                    it.copy(categoryDetailSortOrder = event.sortOrder)
                }
            }
        }
    }

    private fun loadCategoryDetail(categoryId: Int, historyMonths: Int = _state.value.categoryDetailHistoryMonths) {
        categoryDetailJob?.cancel()
        categoryDetailJob = viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoadingCategoryDetail = true,
                    categoryDetailHistoryMonths = historyMonths
                )
            }
            analysisUseCase.getCategoryAnalysisDetail(
                categoryId = categoryId,
                targetMonth = _state.value.selectedMonth,
                type = _state.value.selectedType,
                historyMonths = historyMonths
            ).collect { detail ->
                _state.update {
                    it.copy(
                        categoryDetail = detail,
                        isLoadingCategoryDetail = false,
                        categoryDetailSortOrder = CategoryDetailSortOrder.NEWEST
                    )
                }
            }
        }
    }

    private fun initializeMonth() {
        viewModelScope.launch {
            val currentMonth = periodUseCase.getCurrentPeriodMonth()
            _state.update { it.copy(selectedMonth = currentMonth) }
        }
    }

    private fun observePeriod() {
        viewModelScope.launch {
            periodUseCase.observeFinancialPeriod(state.map { it.selectedMonth })
                .collect { period ->
                    _state.update { it.copy(financialPeriod = period) }
                }
        }
    }

    private fun observeOverview() {
        viewModelScope.launch {
            combine(
                state.map { it.selectedMonth }.distinctUntilChanged(),
                state.map { it.selectedType }.distinctUntilChanged()
            ) { month, type ->
                month to type
            }.flatMapLatest { (month, type) ->
                analysisUseCase.getAnalysisOverview(month, type)
            }.collect { overview ->
                _state.update { current ->
                    val updatedSelection = current.selectedCategory?.let { sel ->
                        overview.categorySpendings.firstOrNull { it.categoryId == sel.categoryId }
                    }
                    current.copy(
                        overview = overview,
                        selectedCategory = updatedSelection
                    )
                }
            }
        }
    }
}
