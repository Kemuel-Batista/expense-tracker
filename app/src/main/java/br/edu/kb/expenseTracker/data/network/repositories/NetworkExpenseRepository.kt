package br.edu.kb.expenseTracker.data.network.repositories

import br.edu.kb.expenseTracker.data.model.Expense
import br.edu.kb.expenseTracker.data.model.ExpenseSummary
import br.edu.kb.expenseTracker.data.repositories.IExpenseRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class NetworkExpenseRepository : IExpenseRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val expenseCollection = firestore.collection("expenses")

    override fun getAllExpenses(): Flow<List<Expense>> = callbackFlow {
        val listener = expenseCollection.addSnapshotListener {
                data, errors ->

            if (errors != null) {
                close(errors)
                return@addSnapshotListener
            }

            if (data != null) {
                val expenses = data.documents.mapNotNull {
                    it.toObject(Expense::class.java)
                }
                trySend(expenses).isSuccess
            }
        }

        awaitClose { listener.remove() }
    }

    override fun getTopExpenses(): Flow<List<Expense>> = callbackFlow {
        val listener = expenseCollection
            .whereEqualTo("type", "Expense")
            .orderBy("amount", Query.Direction.DESCENDING)
            .limit(5)
            .addSnapshotListener {
                    data, errors ->

                if (errors != null) {
                    close(errors)
                    return@addSnapshotListener
                }

                if (data != null) {
                    val expenses = data.documents.mapNotNull {
                        it.toObject(Expense::class.java)
                    }
                    trySend(expenses).isSuccess
                }
            }

        awaitClose { listener.remove() }
    }

    override fun getAllExpenseByDate(type: String): Flow<List<ExpenseSummary>> = callbackFlow {
        val listener = expenseCollection
            .whereEqualTo("type", type)
            .orderBy("date")
            .addSnapshotListener { data, errors ->

                if (errors != null) {
                    close(errors)
                    return@addSnapshotListener
                }

                if (data != null) {
                    val expensesByDate = data.documents
                        .mapNotNull { it.toObject(Expense::class.java) }
                        .groupBy { it.date }
                        .map { (date, expenses) ->
                            ExpenseSummary(
                                type = type,
                                date = date,
                                total_amount = expenses.sumOf { it.amount }
                            )
                        }

                    trySend(expensesByDate).isSuccess
                }
            }

        awaitClose { listener.remove() }
    }

    private suspend fun getId(): Int {
        val data = expenseCollection.get().await()
        val maxId = data.documents.mapNotNull {
            it.getLong("id")?.toInt()
        }.maxOrNull() ?: 0
        return maxId + 1
    }

    override suspend fun insertExpense(expense: Expense) {
        expense.id = getId()
        val document = expenseCollection.document(expense.id.toString())

        document.set(expense).await()
    }

    override suspend fun deleteExpense(expense: Expense) {
        expenseCollection.document(expense.id.toString()).delete().await()
    }

    override suspend fun updateExpense(expense: Expense) {
        val document = expenseCollection.document(expense.id.toString())

        document.set(expense).await()
    }
}