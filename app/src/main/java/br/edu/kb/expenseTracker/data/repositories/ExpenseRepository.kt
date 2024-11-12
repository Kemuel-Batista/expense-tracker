package br.edu.kb.expenseTracker.data.repositories

import br.edu.kb.expenseTracker.data.model.Expense
import br.edu.kb.expenseTracker.data.model.ExpenseSummary
import br.edu.kb.expenseTracker.data.local.repositories.LocalExpenseRepository
import br.edu.kb.expenseTracker.data.network.queues.NetworkQueue
import br.edu.kb.expenseTracker.data.network.repositories.NetworkExpenseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(
    private val localDataSource: LocalExpenseRepository,
    private val remoteDataSource: NetworkExpenseRepository
) : IExpenseRepository {
    private val networkQueue = NetworkQueue(CoroutineScope(Dispatchers.IO))

    override fun getAllExpenses(): Flow<List<Expense>> {
        val expenses = localDataSource.getAllExpenses()
        return expenses
    }

    override fun getTopExpenses(): Flow<List<Expense>> {
        val expenses = localDataSource.getTopExpenses()
        return expenses
    }

    override fun getAllExpenseByDate(type: String): Flow<List<ExpenseSummary>> {
        val expenses = localDataSource.getAllExpenseByDate(type)
        return expenses
    }

    override suspend fun insertExpense(expense: Expense) {
        localDataSource.insertExpense(expense)

        networkQueue.add { remoteDataSource.insertExpense(expense) }
    }

    override suspend fun deleteExpense(expense: Expense) {
        localDataSource.deleteExpense(expense)

        networkQueue.add { remoteDataSource.deleteExpense(expense) }
    }

    override suspend fun updateExpense(expense: Expense) {
        localDataSource.updateExpense(expense)

        networkQueue.add { remoteDataSource.updateExpense(expense) }
    }
}