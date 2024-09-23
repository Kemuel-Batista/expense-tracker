package br.edu.kb.expenseTracker.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.edu.kb.expenseTracker.data.ExpenseDatabase
import br.edu.kb.expenseTracker.data.dao.ExpenseDao
import br.edu.kb.expenseTracker.data.model.ExpenseEntity
import br.edu.kb.expenseTracker.R
import br.edu.kb.expenseTracker.Utils

class HomeViewModel(dao: ExpenseDao) : ViewModel() {
    val expenses = dao.getAllExpenses()

    fun getBalance(list : List<ExpenseEntity>) : String {
        var balance = 0.0
        for (expense in list) {
            if (expense.type == "Income") {
                balance += expense.amount
            } else {
                balance -= expense.amount
            }
        }
        return "$ ${Utils.formatToDecimalValue(balance)}"
    }

    fun getTotalExpense(list : List<ExpenseEntity>) : String {
        var total = 0.0
        for (expense in list) {
            if (expense.type != "Income") {
                total += expense.amount
            }
        }

        return "$ ${Utils.formatToDecimalValue(total)}"
    }

    fun getTotalIncome(list : List<ExpenseEntity>) : String {
        var totalIncome = 0.0
        for (expense in list) {
            if (expense.type == "Income") {
                totalIncome += expense.amount
            }
        }

        return "$ ${Utils.formatToDecimalValue(totalIncome)}"
    }
}

class HomeViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            val dao = ExpenseDatabase.getDatabase(context).expenseDao()
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}