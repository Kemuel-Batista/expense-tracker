package br.edu.kb.expenseTracker.data.repositories

import br.edu.kb.expenseTracker.data.model.ExpenseEntity
import br.edu.kb.expenseTracker.data.model.ExpenseSummary
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class RemoteExpenseRepository : IExpenseRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val expenseCollection = firestore.collection("expenses")

    override fun getAllExpenses(): Flow<List<ExpenseEntity>> = callbackFlow {
        val listener = expenseCollection.addSnapshotListener {
            data, errors ->

            if (errors != null) {
                close(errors)
                return@addSnapshotListener
            }

            if (data != null) {
                val expenses = data.documents.mapNotNull {
                    it.toObject(ExpenseEntity::class.java)
                }
                trySend(expenses).isSuccess
            }
        }

        awaitClose { listener.remove() }
    }

    override fun getTopExpenses(): Flow<List<ExpenseEntity>> = callbackFlow {
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
                        it.toObject(ExpenseEntity::class.java)
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
                        .mapNotNull { it.toObject(ExpenseEntity::class.java) }
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

    override suspend fun insertExpense(expenseEntity: ExpenseEntity) {
        expenseEntity.id = getId()
        val document = expenseCollection.document(expenseEntity.id.toString())

        document.set(expenseEntity).await()
    }

    override suspend fun deleteExpense(expenseEntity: ExpenseEntity) {
        expenseCollection.document(expenseEntity.id.toString()).delete().await()
    }

    override suspend fun updateExpense(expenseEntity: ExpenseEntity) {
        val document = expenseCollection.document(expenseEntity.id.toString())

        document.set(expenseEntity).await()
    }
}