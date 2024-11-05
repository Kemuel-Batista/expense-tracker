package br.edu.kb.expenseTracker.data.repositories

import br.edu.kb.expenseTracker.data.dao.ExpenseDao
import br.edu.kb.expenseTracker.data.model.ExpenseEntity
import br.edu.kb.expenseTracker.data.model.ExpenseSummary
import kotlinx.coroutines.flow.Flow

class LocalExpenseRepository(private val dao: ExpenseDao): IExpenseRepository {
    override fun getAllExpenses(): Flow<List<ExpenseEntity>> {
        return dao.getAllExpenses()
    }

    override fun getTopExpenses(): Flow<List<ExpenseEntity>> {
        return dao.getTopExpenses()
    }

    override fun getAllExpenseByDate(type: String): Flow<List<ExpenseSummary>> {
        return dao.getAllExpenseByDate()
    }

    override suspend fun insertExpense(expenseEntity: ExpenseEntity) {
        dao.insertExpense(expenseEntity)
    }

    override suspend fun deleteExpense(expenseEntity: ExpenseEntity) {
        dao.deleteExpense(expenseEntity)
    }

    override suspend fun updateExpense(expenseEntity: ExpenseEntity) {
       dao.updateExpense(expenseEntity)
    }

}