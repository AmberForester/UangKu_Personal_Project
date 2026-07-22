package com.example.uangku.feature.category.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uangku.feature.category.domain.CategoryUseCase
import com.example.uangku.feature.category.domain.Category
import com.example.uangku.core.domain.Type
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoryViewModel (

    private val categoryUseCase: CategoryUseCase

) : ViewModel() {

    private val _state = MutableStateFlow(CategoryState())
    val state = _state.asStateFlow()

    fun onEvent(event: CategoryEvent){

        when(event){
            
            CategoryEvent.onAddClick -> { showCreateDialog() }

            is CategoryEvent.onEditClick -> { showEditDialog(event.category) }

            is CategoryEvent.onDeleteClick -> { showDeleteDialog(event.category) }

            CategoryEvent.onDismissFormDialog -> { dismissFormDialog() }

            CategoryEvent.onDismissDeleteDialog -> { dismissDeleteDialog() }

            is CategoryEvent.onNameChange ->
                _state.update {
                    it.copy(name = event.value)
                }

            is CategoryEvent.onIconChange ->
                _state.update {
                    it.copy(icon = event.value)
                }

            is CategoryEvent.onTypeChange ->
                _state.update {
                    it.copy(type = event.value)
                }

            CategoryEvent.onSave -> { saveCategory() }

            CategoryEvent.onDeleteConfirm -> { deleteCategory() }

            CategoryEvent.onScreenOpen -> { loadCategories() }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryUseCase.getCategories().collect { categories ->

                Log.d("Category", "size = ${categories.size}")

                _state.update {
                    it.copy(
                        categories = categories
                    )
                }
            }
        }
    }

    private fun showCreateDialog() {
        _state.update {
            it.copy(
                selectedCategory = null,
                name = "",
                icon = "",
                type = Type.EXPENSE,
                showFormDialog = true
            )
        }
    }

    private fun showEditDialog(category: Category) {
        _state.update {
            it.copy(
                selectedCategory = category,
                name = category.name,
                type = category.type,
                showFormDialog = true
            )
        }
    }

    private fun showDeleteDialog(category: Category) {
        _state.update {
            it.copy(
                selectedCategory = category,
                showDeleteDialog = true
            )
        }
    }

    private fun dismissFormDialog() {
        _state.update {
            it.copy(
                showFormDialog = false
            )
        }
    }

    private fun dismissDeleteDialog() {
        _state.update {
            it.copy(
                showDeleteDialog = false
            )
        }
    }

    private fun saveCategory() {
        viewModelScope.launch {
            val category = Category(
                id = _state.value.selectedCategory?.id ?: 0,
                name = _state.value.name,
                type = _state.value.type
            )
            if (_state.value.selectedCategory == null) {
                categoryUseCase.createCategory(category)
            } else {
                categoryUseCase.updateCategory(category)
            }
            dismissFormDialog()
            loadCategories()
        }
    }

    private fun deleteCategory() {
        viewModelScope.launch {
            _state.value.selectedCategory?.let {
                categoryUseCase.deleteCategory(it)
            }
            dismissDeleteDialog()
            loadCategories()
        }
    }
}