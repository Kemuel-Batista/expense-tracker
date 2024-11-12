package br.edu.kb.expenseTracker.data.local.repositories

import br.edu.kb.expenseTracker.data.local.dao.ExpenseDao
import br.edu.kb.expenseTracker.data.mappers.asExternalModel
import br.edu.kb.expenseTracker.data.mappers.toEntity
import br.edu.kb.expenseTracker.data.model.Expense
import br.edu.kb.expenseTracker.data.model.ExpenseSummary
import br.edu.kb.expenseTracker.data.repositories.IExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalExpenseRepository(private val dao: ExpenseDao): IExpenseRepository {
    override fun getAllExpenses(): Flow<List<Expense>> {
        return dao.getAllExpenses().map { list -> list.map { it.asExternalModel() } }
    }

    override fun getTopExpenses(): Flow<List<Expense>> {
        return dao.getTopExpenses().map { list -> list.map { it.asExternalModel() } }
    }

    override fun getAllExpenseByDate(type: String): Flow<List<ExpenseSummary>> {
        return dao.getAllExpenseByDate()
    }

    override suspend fun insertExpense(expense: Expense) {
        dao.insertExpense(expense.toEntity())
    }

    override suspend fun deleteExpense(expense: Expense) {
        dao.deleteExpense(expense.toEntity())
    }

    override suspend fun updateExpense(expense: Expense) {
        dao.updateExpense(expense.toEntity())
    }
}