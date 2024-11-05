package br.edu.kb.expenseTracker.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.edu.kb.expenseTracker.data.dao.ExpenseDao
import br.edu.kb.expenseTracker.data.model.ExpenseEntity

@Database(entities = [ExpenseEntity::class], version = 1)
abstract class ExpenseDB : RoomDatabase() {
    abstract fun getExpenseDao(): ExpenseDao
}

fun openDatabase(context: Context): ExpenseDB {
    return Room.databaseBuilder(
        context.applicationContext,
        ExpenseDB::class.java,
        name = "arquivo.db"
    ).build()
}