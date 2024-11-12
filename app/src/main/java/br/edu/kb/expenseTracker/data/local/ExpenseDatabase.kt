package br.edu.kb.expenseTracker.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.edu.kb.expenseTracker.data.local.dao.ExpenseDao
import br.edu.kb.expenseTracker.data.local.entities.ExpenseEntity

@Database(entities = [ExpenseEntity::class], version = 1)
abstract class ExpenseDB : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
}

fun openDatabase(context: Context): ExpenseDB {
    return Room.databaseBuilder(
        context.applicationContext,
        ExpenseDB::class.java,
        name = "arquivo.db"
    ).build()
}