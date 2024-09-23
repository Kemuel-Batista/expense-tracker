package br.edu.kb.expenseTracker.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.edu.kb.expenseTracker.data.ExpenseDatabase
import br.edu.kb.expenseTracker.data.dao.ExpenseDao
import br.edu.kb.expenseTracker.Utils
import com.github.mikephil.charting.data.Entry
import br.edu.kb.expenseTracker.data.model.ExpenseSummary

class StatsViewModel(dao: ExpenseDao) : ViewModel() {
    val entries = dao.getAllExpenseByDate()
    val topEntries =  dao.getTopExpenses()

    fun getEntriesForChart(entries: List<ExpenseSummary>): List<Entry> {
        val list = mutableListOf<Entry>()
        for (entry in entries) {
            val formattedDate = Utils.getMillisFromDate(entry.date)
            list.add(Entry(formattedDate.toFloat(), entry.total_amount.toFloat()))
        }
        return list
    }
}

class StatsViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatsViewModel::class.java)) {
            val dao = ExpenseDatabase.getDatabase(context).expenseDao()
            return StatsViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}