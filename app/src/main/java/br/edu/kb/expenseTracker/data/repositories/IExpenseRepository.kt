package br.edu.kb.expenseTracker.data.repositories

import br.edu.kb.expenseTracker.data.model.ExpenseEntity
import br.edu.kb.expenseTracker.data.model.ExpenseSummary
import kotlinx.coroutines.flow.Flow

interface IExpenseRepository {
    fun getAllExpenses(): Flow<List<ExpenseEntity>>
    fun getTopExpenses(): Flow<List<ExpenseEntity>>
    fun getAllExpenseByDate(type: String = "Expense"): Flow<List<ExpenseSummary>>
    suspend fun insertExpense(expenseEntity: ExpenseEntity)
    suspend fun deleteExpense(expenseEntity: ExpenseEntity)
    suspend fun updateExpense(expenseEntity: ExpenseEntity)
}