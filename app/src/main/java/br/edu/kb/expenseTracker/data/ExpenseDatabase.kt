package br.edu.kb.expenseTracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import br.edu.kb.expenseTracker.data.dao.ExpenseDao
import br.edu.kb.expenseTracker.data.migrations.MIGRATION_1_2
import br.edu.kb.expenseTracker.data.model.ExpenseEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [ExpenseEntity::class],
    version = 2,
    exportSchema = true,
)
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
            )
            .addMigrations(MIGRATION_1_2)
            .addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // initBasicData(context)
                }

                fun initBasicData(context: Context) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val dao = getDatabase(context).expenseDao()
                        dao.insertExpense(ExpenseEntity(1, "Salary", 5000.40, "2024-08-02", "Salary", "Income"))
                        dao.insertExpense(ExpenseEntity(2, "Paypal", 2308.53, "2024-08-02", "Paypal", "Income"))
                        dao.insertExpense(ExpenseEntity(3, "Netflix", 100.43, "2024-08-02", "Netflix", "Expense"))
                        dao.insertExpense(ExpenseEntity(4, "Netflix", 400.56, "2024-08-02", "Netflix", "Expense"))
                    }
                }
            }).build()
        }
    }
}