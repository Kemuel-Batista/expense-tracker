package br.edu.kb.expenseTracker.data.network.queues

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentLinkedQueue

class NetworkQueue(
    private val coroutineScope: CoroutineScope,
    private val retryDelay: Long = 5000 // Retry delay in milliseconds
) {
    private val queue = ConcurrentLinkedQueue<suspend () -> Unit>()
    private var isProcessing = false

    // Adds a network operation to the queue
    fun add(operation: suspend () -> Unit) {
        queue.add(operation)
        processQueue()
    }

    // Processes the queued operations
    private fun processQueue() {
        if (isProcessing) return
        isProcessing = true

        coroutineScope.launch {
            while (queue.isNotEmpty()) {
                val operation = queue.poll()
                try {
                    operation?.invoke()
                } catch (e: Exception) {
                    // If a network error occurs, re-add the operation and retry later
                    if (operation != null) queue.add(operation)
                    delay(retryDelay) // Wait before retrying
                }
            }
            isProcessing = false
        }
    }
}