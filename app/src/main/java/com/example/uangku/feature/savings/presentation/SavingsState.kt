package com.example.uangku.feature.savings.presentation

import com.example.uangku.feature.savings.domain.SavingGoal
import com.example.uangku.feature.savings.domain.SavingLog
import com.example.uangku.feature.savings.domain.SavingType
import java.util.Date

data class SavingsState (

    val goals: List<SavingGoal> = emptyList(),
    val totalSavings: Double = 0.0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    // Detail Screen State
    val currentDetailGoal: SavingGoal? = null,

    // Dialog Tambah / Edit Target
    val showGoalDialog: Boolean = false,
    val selectedGoal: SavingGoal? = null, // null jika tambah baru, ada isi jika edit
    val titleInput: String = "",
    val targetAmountInput: String = "",    // Berupa String agar mudah diedit di TextField
    val targetDateInput: Date? = null,

    // Dialog Konfirmasi Hapus Target
    val showDeleteDialog: Boolean = false,
    val goalToDelete: SavingGoal? = null,

    // Dialog Setor (Nabung) & Tarik Dana
    val showActionDialog: Boolean = false,
    val actionGoal: SavingGoal? = null,
    val actionType: SavingType = SavingType.DEPOSIT,
    val actionAmountInput: String = "",
    val actionDescInput: String = "",

    // Dialog / Sheet Riwayat Mutasi (Logs)
    val showLogsDialog: Boolean = false,
    val selectedGoalForLogs: SavingGoal? = null,
    val goalLogs: List<SavingLog> = emptyList()

)