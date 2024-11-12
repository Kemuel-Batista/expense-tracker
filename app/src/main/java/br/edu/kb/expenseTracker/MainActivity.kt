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
import br.edu.kb.expenseTracker.data.local.openDatabase
import br.edu.kb.expenseTracker.data.local.repositories.LocalExpenseRepository
import br.edu.kb.expenseTracker.data.network.repositories.NetworkExpenseRepository
import br.edu.kb.expenseTracker.data.repositories.ExpenseRepository
import br.edu.kb.expenseTracker.data.repositories.IExpenseRepository
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
                    val db = remember { openDatabase(this) }
                    val dao = db.expenseDao()

                    val localExpenseRepository = LocalExpenseRepository(dao)
                    val networkRepository = NetworkExpenseRepository()

                    val repository = ExpenseRepository(
                        localExpenseRepository,
                        networkRepository,
                    )

                    val viewModel = ExpenseViewModel(repository)

                    NavHostScreen(viewModel)
                }
            }
        }
    }
}
