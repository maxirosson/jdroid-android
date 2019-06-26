package com.jdroid.android.listener

import android.text.Editable
import android.text.TextWatcher
import com.jdroid.android.application.AbstractApplication
import com.jdroid.java.concurrent.ExecutorUtils
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit

abstract class RemoteSearchTextWatcher : TextWatcher {

    companion object {
        // The search frequency in milliseconds
        private const val SEARCH_FREQUENCY = 1000
    }

    private lateinit var timerSearchRunnable: TimerSearchRunnable
    private lateinit var timerSearchThread: Thread

    init {
        startWatching()
    }

    fun startWatching() {
        timerSearchRunnable = TimerSearchRunnable()
        timerSearchThread = Thread(timerSearchRunnable)
        timerSearchThread.start()
    }

    fun stopWatching() {
        timerSearchThread.interrupt()
    }

    /**
     * @see android.text.TextWatcher.afterTextChanged
     */
    override fun afterTextChanged(s: Editable) {
        try {
            timerSearchRunnable.queue.put(s.toString())
        } catch (e: InterruptedException) {
            AbstractApplication.get().exceptionHandler.logHandledException(e)
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        // Nothing here by default.
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        // Nothing here by default.
    }

    protected abstract fun onPerformSearch(searchValue: String)

    private inner class TimerSearchRunnable : Runnable {

        val queue = ArrayBlockingQueue<String>(20)

        override fun run() {
            var exit = false
            while (!exit) {
                try {
                    val searchValue = queue.take()
                    if (queue.isEmpty()) {
                        ExecutorUtils.sleep(SEARCH_FREQUENCY, TimeUnit.MILLISECONDS)
                        // If after waiting for a SEARCH_FREQUENCY, no more input was added, the search is performed.
                        if (queue.isEmpty()) {
                            this@RemoteSearchTextWatcher.onPerformSearch(searchValue)
                        }
                    }
                } catch (e: InterruptedException) {
                    exit = true
                }
            }
        }
    }
}
