package com.wirecard.payment.api.infrastructure.concurrency

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.web.context.request.async.DeferredResult

fun <T> defer(exec: () -> T): DeferredResult<T> {
    var result = DeferredResult<T>()

    ExecutorCreator.getExecutor().execute {
        try {
            val execResult = exec()
            result.setResult(execResult)

        } catch (e: Exception) {
            result.setErrorResult(e)
        }
    }

    return result
}

private object ExecutorCreator {

    private val executor = ThreadPoolTaskExecutor()

    init {
        executor.corePoolSize = 2
        executor.maxPoolSize = 2
        executor.setQueueCapacity(20000)
        executor.setThreadNamePrefix("ExecPool-")
        executor.initialize()
    }

    fun getExecutor() = executor

}
