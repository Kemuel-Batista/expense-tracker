package br.edu.kb.expenseTracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.kb.expenseTracker.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import br.edu.kb.expenseTracker.data.model.ExpenseEntity
import br.edu.kb.expenseTracker.data.model.ExpenseSummary
import br.edu.kb.expenseTracker.data.repositories.IExpenseRepository
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.launch

class ExpenseViewModel(private val repository: IExpenseRepository): ViewModel() {
    private val _expenses = MutableStateFlow<List<ExpenseEntity>>(emptyList())
    val expenses: StateFlow<List<ExpenseEntity>> get() = _expenses

    private val _expensesByDate = MutableStateFlow<List<ExpenseSummary>>(emptyList())

    private val _topExpenses = MutableStateFlow<List<ExpenseEntity>>(emptyList())
    val topExpenses: StateFlow<List<ExpenseEntity>> get() = _topExpenses

    init {
        viewModelScope.launch {
            repository.getAllExpenses().collectLatest { list ->
                _expenses.value = list
            }

            repository.getAllExpenseByDate().collectLatest { list ->
                _expensesByDate.value = list
            }
        }
    }

    suspend fun addExpense(expenseEntity: ExpenseEntity): Boolean {
        return try {
            viewModelScope.launch {
                repository.insertExpense(expenseEntity)
            }
            true
        } catch (ex: Throwable) {
            false
        }
    }

    fun getBalance() : String {
        var balance = 0.0
        for (expense in _expenses.value) {
            if (expense.type == "Income") {
                balance += expense.amount
            } else {
                balance -= expense.amount
            }
        }
        return "$ ${Utils.formatToDecimalValue(balance)}"
    }

    fun getTotalExpense() : String {
        var total = 0.0
        for (expense in _expenses.value) {
            if (expense.type != "Income") {
                total += expense.amount
            }
        }

        return "$ ${Utils.formatToDecimalValue(total)}"
    }

    fun getTotalIncome() : String {
        var totalIncome = 0.0
        for (expense in _expenses.value) {
            if (expense.type == "Income") {
                totalIncome += expense.amount
            }
        }

        return "$ ${Utils.formatToDecimalValue(totalIncome)}"
    }

    fun getEntriesForChart(): List<Entry> {
        val list = mutableListOf<Entry>()
        for (entry in _expensesByDate.value) {
            val formattedDate = Utils.getMillisFromDate(entry.date)
            list.add(Entry(formattedDate.toFloat(), entry.total_amount.toFloat()))
        }
        return list
    }
}