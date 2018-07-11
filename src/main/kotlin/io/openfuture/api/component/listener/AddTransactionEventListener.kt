package io.openfuture.api.component.listener

import io.openfuture.api.component.listener.event.AddTransactionEvent
import io.openfuture.api.domain.PageResponse
import io.openfuture.api.domain.transaction.TransactionDto
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import org.springframework.web.context.request.async.DeferredResult
import java.util.concurrent.ConcurrentHashMap

@Component
class AddTransactionEventListener : ApplicationListener<AddTransactionEvent> {

    private val results: ConcurrentHashMap<String, List<DeferredResult<PageResponse<TransactionDto>>>> = ConcurrentHashMap()

    companion object {
        const val RESULT_TIMEOUT = 30000L
    }


    override fun onApplicationEvent(event: AddTransactionEvent) {
        val transaction = event.transaction
        val scaffoldAddress = transaction.scaffold.address

        results[scaffoldAddress]?.forEach { it.setResult(PageResponse(listOf(transaction))) }
        results.remove(scaffoldAddress)
    }

    fun getResult(scaffoldAddress: String): DeferredResult<PageResponse<TransactionDto>> {
        val result = DeferredResult<PageResponse<TransactionDto>>(RESULT_TIMEOUT, PageResponse<TransactionDto>(listOf()))

        val scaffoldResults = results[scaffoldAddress]?.plus(result) ?: mutableListOf(result)
        results[scaffoldAddress] = scaffoldResults.filter { !it.isSetOrExpired }

        return result
    }

}