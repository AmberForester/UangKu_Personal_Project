package com.example.uangku.feature.savings.presentation

import com.example.uangku.feature.savings.domain.SavingGoal
import com.example.uangku.feature.savings.domain.SavingType
import java.util.Date

sealed interface SavingsEvent {

    data object OnScreenOpen : SavingsEvent
    data object ClearError : SavingsEvent

    // Detail Screen
    data class OnDetailScreenOpen(val goalId: Long) : SavingsEvent
    data object OnDetailScreenClose : SavingsEvent

    // Form Target (Tambah / Edit)
    data object OnAddGoalClick : SavingsEvent
    data class OnEditGoalClick(val goal: SavingGoal) : SavingsEvent
    data object OnDismissGoalDialog : SavingsEvent
    data class OnTitleChange(val value: String) : SavingsEvent
    data class OnTargetAmountChange(val value: String) : SavingsEvent
    data class OnTargetDateChange(val value: Date?) : SavingsEvent
    data object OnSaveGoal : SavingsEvent

    // Hapus Target
    data class OnDeleteGoalClick(val goal: SavingGoal) : SavingsEvent
    data object OnDismissDeleteDialog : SavingsEvent
    data object OnConfirmDeleteGoal : SavingsEvent

    // Setor & Tarik Dana
    data class OnActionClick(val goal: SavingGoal, val type: SavingType) : SavingsEvent
    data object OnDismissActionDialog : SavingsEvent
    data class OnActionAmountChange(val value: String) : SavingsEvent
    data class OnActionDescChange(val value: String) : SavingsEvent
    data object OnConfirmAction : SavingsEvent

    // Riwayat Mutasi (Logs)
    data class OnViewLogsClick(val goal: SavingGoal) : SavingsEvent
    data object OnDismissLogsDialog : SavingsEvent
}