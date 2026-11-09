package com.example.uangku.feature.savings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uangku.feature.savings.domain.SavingGoal
import com.example.uangku.feature.savings.domain.SavingType
import com.example.uangku.feature.savings.domain.SavingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SavingsViewModel (

    private val savingsUseCase: SavingsUseCase

) : ViewModel() {
    private val _state = MutableStateFlow(SavingsState())
    val state = _state.asStateFlow()

    fun onEvent(event: SavingsEvent) {
        when(event) {
            SavingsEvent.OnScreenOpen -> loadData()
            SavingsEvent.ClearError -> _state.update { it.copy(errorMessage = null) }

            // === Detail Screen ===
            is SavingsEvent.OnDetailScreenOpen -> loadDetail(event.goalId)
            SavingsEvent.OnDetailScreenClose -> {
                _state.update {
                    it.copy(
                        currentDetailGoal = null,
                        goalLogs = emptyList()
                    )
                }
            }

            // === Form Target ===
            SavingsEvent.OnAddGoalClick -> {
                _state.update {
                    it.copy(
                        showGoalDialog = true,
                        selectedGoal = null,
                        titleInput = "",
                        targetAmountInput = "",
                        targetDateInput = null
                    )
                }
            }

            is SavingsEvent.OnEditGoalClick -> {
                val goal = event.goal
                _state.update {
                    it.copy(
                        showGoalDialog = true,
                        selectedGoal = goal,
                        titleInput = goal.title,
                        targetAmountInput = goal.targetAmount?.let { amount ->
                            if (amount % 1.0 == 0.0) amount.toLong().toString() else amount.toString()
                        } ?: "",
                        targetDateInput = goal.targetDate
                    )
                }
            }

            SavingsEvent.OnDismissGoalDialog -> {
                _state.update { it.copy(showGoalDialog = false) }
            }

            is SavingsEvent.OnTitleChange -> {
                _state.update { it.copy(titleInput = event.value) }
            }

            is SavingsEvent.OnTargetAmountChange -> {
                _state.update { it.copy(targetAmountInput = event.value) }
            }

            is SavingsEvent.OnTargetDateChange -> {
                _state.update { it.copy(targetDateInput = event.value) }
            }

            SavingsEvent.OnSaveGoal -> saveGoal()

            // === Hapus Target ===
            is SavingsEvent.OnDeleteGoalClick -> {
                _state.update {
                    it.copy(
                        showDeleteDialog = true,
                        goalToDelete = event.goal
                    )
                }
            }

            SavingsEvent.OnDismissDeleteDialog -> {
                _state.update {
                    it.copy(
                        showDeleteDialog = false,
                        goalToDelete = null
                    )
                }
            }

            SavingsEvent.OnConfirmDeleteGoal -> deleteGoal()

            // === Setor & Tarik Dana ===
            is SavingsEvent.OnActionClick -> {
                _state.update {
                    it.copy(
                        showActionDialog = true,
                        actionGoal = event.goal,
                        actionType = event.type,
                        actionAmountInput = "",
                        actionDescInput = ""
                    )
                }
            }

            SavingsEvent.OnDismissActionDialog -> {
                _state.update {
                    it.copy(
                        showActionDialog = false,
                        actionGoal = null
                    )
                }
            }

            is SavingsEvent.OnActionAmountChange -> {
                _state.update { it.copy(actionAmountInput = event.value) }
            }

            is SavingsEvent.OnActionDescChange -> {
                _state.update { it.copy(actionDescInput = event.value) }
            }

            SavingsEvent.OnConfirmAction -> executeAction()

            // === Riwayat Mutasi (Logs) ===
            is SavingsEvent.OnViewLogsClick -> loadLogs(event.goal)
            SavingsEvent.OnDismissLogsDialog -> {
                _state.update {
                    it.copy(
                        showLogsDialog = false,
                        selectedGoalForLogs = null,
                        goalLogs = emptyList()
                    )
                }
            }
        }
    }
    private fun loadData() {
        viewModelScope.launch {
            // Ambil semua target tabungan
            savingsUseCase.getAllGoals().collect { goalsList ->
                _state.update { it.copy(goals = goalsList) }
            }
        }
        viewModelScope.launch {
            // Ambil akumulasi total tabungan
            savingsUseCase.getTotalLockedSavings().collect { total ->
                _state.update { it.copy(totalSavings = total) }
            }
        }
    }

    private fun loadDetail(goalId: Long) {
        viewModelScope.launch {
            savingsUseCase.getGoalById(goalId).collect { goal ->
                _state.update { it.copy(currentDetailGoal = goal) }
            }
        }
        viewModelScope.launch {
            savingsUseCase.getLogsByGoalId(goalId).collect { logs ->
                _state.update { it.copy(goalLogs = logs) }
            }
        }
    }

    private fun saveGoal() {
        val title = _state.value.titleInput.trim()
        if (title.isBlank()) {
            _state.update { it.copy(errorMessage = "Nama tabungan tidak boleh kosong") }
            return
        }

        val targetAmount = _state.value.targetAmountInput.toDoubleOrNull()
        val targetDate = _state.value.targetDateInput
        val selectedGoal = _state.value.selectedGoal

        viewModelScope.launch {
            try {
                if (selectedGoal == null) {
                    // Tambah baru
                    val newGoal = SavingGoal(
                        title = title,
                        targetAmount = targetAmount,
                        targetDate = targetDate
                    )
                    savingsUseCase.createGoal(newGoal)
                } else {
                    // Update yang sudah ada
                    val updated = selectedGoal.copy(
                        title = title,
                        targetAmount = targetAmount,
                        targetDate = targetDate
                    )
                    savingsUseCase.updateGoal(updated)
                }
                _state.update { it.copy(showGoalDialog = false) }
            } catch (e: Exception) {
                _state.update { it.copy(errorMessage = e.localizedMessage ?: "Gagal menyimpan target") }
            }
        }
    }

    private fun deleteGoal() {
        val goal = _state.value.goalToDelete ?: return
        val goalId = goal.id ?: return

        viewModelScope.launch {
            try {
                savingsUseCase.deleteGoal(goalId)
                _state.update {
                    it.copy(
                        showDeleteDialog = false,
                        goalToDelete = null,
                        currentDetailGoal = if (it.currentDetailGoal?.id == goalId) null else it.currentDetailGoal
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(errorMessage = e.localizedMessage ?: "Gagal menghapus target") }
            }
        }
    }

    private fun executeAction() {
        val goal = _state.value.actionGoal ?: return
        val goalId = goal.id ?: return
        val amount = _state.value.actionAmountInput.toDoubleOrNull()

        if (amount == null || amount <= 0) {
            _state.update { it.copy(errorMessage = "Nominal harus lebih besar dari 0") }
            return
        }

        val note = _state.value.actionDescInput.trim().ifBlank { null }
        val type = _state.value.actionType

        viewModelScope.launch {
            try {
                when (type) {
                    SavingType.DEPOSIT -> savingsUseCase.deposit(goalId, amount, note)
                    SavingType.WITHDRAW -> savingsUseCase.withdraw(goalId, amount, note)
                }
                _state.update {
                    it.copy(
                        showActionDialog = false,
                        actionGoal = null
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(errorMessage = e.localizedMessage ?: "Gagal memproses transaksi") }
            }
        }
    }

    private fun loadLogs(goal: SavingGoal) {
        val goalId = goal.id ?: return
        _state.update {
            it.copy(
                showLogsDialog = true,
                selectedGoalForLogs = goal
            )
        }
        viewModelScope.launch {
            savingsUseCase.getLogsByGoalId(goalId).collect { logs ->
                _state.update { it.copy(goalLogs = logs) }
            }
        }
    }
}