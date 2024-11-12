package br.edu.kb.expenseTracker.data.mappers

import br.edu.kb.expenseTracker.data.local.entities.ExpenseEntity
import br.edu.kb.expenseTracker.data.model.Expense
import br.edu.kb.expenseTracker.data.network.models.NetworkExpense

fun NetworkExpense.asEntity() = ExpenseEntity(
    id = id,
    title = title,
    amount = amount,
    date = date,
    category = category,
    type = type,
)

fun ExpenseEntity.asExternalModel() = Expense(
    id = id,
    title = title,
    amount = amount,
    date = date,
    category = category,
    type = type,
)

fun Expense.toEntity() = ExpenseEntity(
    id = id,
    title = title,
    amount = amount,
    date = date,
    category = category,
    type = type
)