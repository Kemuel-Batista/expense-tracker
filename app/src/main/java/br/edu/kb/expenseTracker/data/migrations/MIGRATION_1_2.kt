package br.edu.kb.expenseTracker.data.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Passo 1: Deletar a tabela antiga
        database.execSQL("DROP TABLE IF EXISTS expense_table")

        // Passo 2: Criar a nova tabela com a nova estrutura
        database.execSQL("""
            CREATE TABLE expense_table (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                amount REAL NOT NULL,
                date TEXT NOT NULL,
                category TEXT NOT NULL,
                type TEXT NOT NULL
            )
        """)
    }
}
