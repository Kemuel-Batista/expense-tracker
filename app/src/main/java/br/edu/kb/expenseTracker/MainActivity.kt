package br.edu.kb.expenseTracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import br.edu.kb.expenseTracker.data.db.openDatabase
import br.edu.kb.expenseTracker.data.repositories.IExpenseRepository
import br.edu.kb.expenseTracker.data.repositories.LocalExpenseRepository
import br.edu.kb.expenseTracker.data.repositories.RemoteExpenseRepository
import br.edu.kb.expenseTracker.ui.theme.ExpenseTrackerTheme
import br.edu.kb.expenseTracker.viewmodel.ExpenseViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpenseTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    val isLocal = false

                    val repository: IExpenseRepository
                    if (isLocal){
                        val db = remember { openDatabase(this) }
                        val dao = db.getExpenseDao()
                        repository = LocalExpenseRepository(dao)
                    } else {
                        repository = RemoteExpenseRepository()
                    }
                    val viewModel = ExpenseViewModel(repository)

                    NavHostScreen(viewModel)
                }
            }
        }
    }
}
