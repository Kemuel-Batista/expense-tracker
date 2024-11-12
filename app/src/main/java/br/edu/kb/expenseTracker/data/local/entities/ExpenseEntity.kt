package br.edu.kb.expenseTracker.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expense_table")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    val title: String,
    val amount: Double,
    val date: String,
    val category: String,
    val type: String
) {
    constructor(): this(null, "", 0.0, "", "", "")
}