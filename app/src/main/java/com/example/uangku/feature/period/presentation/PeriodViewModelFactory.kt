package com.example.uangku.feature.period.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uangku.feature.period.domain.PeriodUseCase

@RequiresApi(Build.VERSION_CODES.O)
class PeriodViewModelFactory (
    private val periodUseCase: PeriodUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create (
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(PeriodViewModel::class.java)) {
            return PeriodViewModel(periodUseCase) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }

}