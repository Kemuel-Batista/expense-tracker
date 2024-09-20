package br.edu.kb.expenseTracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import br.edu.kb.expenseTracker.data.dao.ExpenseDao
import br.edu.kb.expenseTracker.data.model.ExpenseEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [ExpenseEntity::class], version = 1)
abstract class ExpenseDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao

    companion object {
        const val DATABASE_NAME = "expense_database"

        @JvmStatic
        fun getDatabase(context: Context): ExpenseDatabase {
            return Room.databaseBuilder(
                context,
                ExpenseDatabase::class.java,
                DATABASE_NAME
            ).addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                }

                fun initBasicData(context: Context) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val dao = getDatabase(context).expenseDao()
                        dao.insertExpense(ExpenseEntity(1, "Salary", 5000.40, System.currentTimeMillis(), "Salary", "Income"))
                        dao.insertExpense(ExpenseEntity(2, "Paypal", 2308.53, System.currentTimeMillis(), "Paypal", "Income"))
                        dao.insertExpense(ExpenseEntity(3, "Netflix", 100.43, System.currentTimeMillis(), "Netflix", "Expense"))
                        dao.insertExpense(ExpenseEntity(4, "Netflix", 400.56, System.currentTimeMillis(), "Netflix", "Expense"))
                    }
                }
            }).build()
        }
    }
}