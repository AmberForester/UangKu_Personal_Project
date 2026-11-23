package com.example.uangku.feature.analysis.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uangku.feature.analysis.domain.AnalysisUseCase
import com.example.uangku.feature.period.domain.PeriodUseCase

class AnalysisViewModelFactory(
    private val analysisUseCase: AnalysisUseCase,
    private val periodUseCase: PeriodUseCase
) : ViewModelProvider.Factory {

    @RequiresApi(Build.VERSION_CODES.O)
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnalysisViewModel::class.java)) {
            return AnalysisViewModel(analysisUseCase, periodUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
