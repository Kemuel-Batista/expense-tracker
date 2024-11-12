package br.edu.kb.expenseTracker.data.model

data class Expense(
    var id: Int?,
    val title: String,
    val amount: Double,
    val date: String,
    val category: String,
    val type: String
)
