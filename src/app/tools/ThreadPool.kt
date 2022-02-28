package app.tools

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class ThreadPool {
    var runnables = arrayListOf<Runnable>()
    var executor = Executors.newFixedThreadPool(20)!!
    fun run() {
        runnables.forEach { runnable -> executor.execute(runnable) }
        executor.shutdown()
        executor.awaitTermination(java.lang.Long.MAX_VALUE, TimeUnit.MINUTES)
    }

    fun newFixedThreadPool(nThreads: Int) {
        executor = Executors.newFixedThreadPool(nThreads)
    }

    fun skip() {
        executor.shutdownNow()
    }


}




