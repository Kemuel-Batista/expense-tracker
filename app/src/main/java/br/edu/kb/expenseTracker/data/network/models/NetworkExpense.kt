package br.edu.kb.expenseTracker.data.network.models

data class NetworkExpense(
    var id: Int?,
    val title: String,
    val amount: Double,
    val date: String,
    val category: String,
    val type: String
)