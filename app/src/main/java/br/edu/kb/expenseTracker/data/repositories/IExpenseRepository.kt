package br.edu.kb.expenseTracker.data.repositories

import br.edu.kb.expenseTracker.data.model.Expense
import br.edu.kb.expenseTracker.data.model.ExpenseSummary
import kotlinx.coroutines.flow.Flow

interface IExpenseRepository {
    fun getAllExpenses(): Flow<List<Expense>>
    fun getTopExpenses(): Flow<List<Expense>>
    fun getAllExpenseByDate(type: String = "Expense"): Flow<List<ExpenseSummary>>
    suspend fun insertExpense(expense: Expense)
    suspend fun deleteExpense(expense: Expense)
    suspend fun updateExpense(expense: Expense)
}