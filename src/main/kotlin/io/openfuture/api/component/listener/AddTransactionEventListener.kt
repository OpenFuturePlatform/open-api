package io.openfuture.api.component.listener

import io.openfuture.api.component.listener.event.AddTransactionEvent
import io.openfuture.api.domain.PageResponse
import io.openfuture.api.domain.transaction.TransactionDto
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import org.springframework.web.context.request.async.DeferredResult
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

@Component
class AddTransactionEventListener : ApplicationListener<AddTransactionEvent> {

    private val promises = ConcurrentHashMap<String, CopyOnWriteArrayList<DeferredResult<PageResponse<TransactionDto>>>>()

    companion object {
        private const val RESULT_TIMEOUT = 30000L
    }


    override fun onApplicationEvent(event: AddTransactionEvent) {
        val transaction = event.transaction
        val scaffoldAddress = transaction.scaffold.address

        promises[scaffoldAddress]?.forEach { it.setResult(PageResponse(listOf(transaction))) }
        promises.remove(scaffoldAddress)
    }

    fun getResult(scaffoldAddress: String): DeferredResult<PageResponse<TransactionDto>> {
        val promise = DeferredResult<PageResponse<TransactionDto>>(RESULT_TIMEOUT, PageResponse<TransactionDto>(listOf()))
        promise.onCompletion { promises[scaffoldAddress]?.remove(promise) }

        if (null == promises[scaffoldAddress]) {
            promises[scaffoldAddress] = CopyOnWriteArrayList()
        }

        promises[scaffoldAddress]!!.add(promise)

        return promise
    }

}