package br.edu.kb.expenseTracker.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.edu.kb.expenseTracker.data.ExpenseDatabase
import br.edu.kb.expenseTracker.data.dao.ExpenseDao
import br.edu.kb.expenseTracker.data.model.ExpenseEntity

data class AddExpenseViewModel(val dao: ExpenseDao): ViewModel() {
    suspend fun addExpense(expenseEntity: ExpenseEntity): Boolean {
        return try {
            dao.insertExpense(expenseEntity)
            true
        } catch (ex: Throwable) {
            false
        }
    }
}

class AddExpenseViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddExpenseViewModel::class.java)) {
            val dao = ExpenseDatabase.getDatabase(context).expenseDao()
            return AddExpenseViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}