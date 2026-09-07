package com.example.uangku.feature.period.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uangku.feature.period.domain.PeriodUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class PeriodViewModel (
    private val periodUseCase: PeriodUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PeriodState())
    val state = _state.asStateFlow()

    fun onEvent(event: PeriodEvent) {

        when(event) {

            PeriodEvent.onScreenOpen -> {
                loadPeriodSettings()
            }

            is PeriodEvent.onStartDayChange -> {
                _state.update {
                    it.copy(
                        selectedStartDay = event.startDay
                    )
                }
            }

            PeriodEvent.onSave -> {
                saveStartDay()
            }
        }
    }

    private fun loadPeriodSettings() {

        viewModelScope.launch {
            periodUseCase.getPeriodSettings().collect { settings ->

                _state.update {
                    it.copy(
                        startDay = settings.startDay,
                        selectedStartDay = settings.startDay
                    )
                }
                loadFinancialPeriod()
            }
        }
    }

    private fun loadFinancialPeriod() {
        viewModelScope.launch {
            val financialPeriod = periodUseCase.getFinancialPeriod()

            _state.update {
                it.copy(
                    financialPeriod = financialPeriod
                )
            }
        }
    }

    private fun saveStartDay() {

        viewModelScope.launch {

            periodUseCase.updateStartDay(_state.value.selectedStartDay)

            _state.update {
                it.copy(
                    startDay = _state.value.selectedStartDay
                )
            }
            loadFinancialPeriod()
        }
    }
}