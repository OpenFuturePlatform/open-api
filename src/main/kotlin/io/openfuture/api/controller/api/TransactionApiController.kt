package io.openfuture.api.controller.api

import io.openfuture.api.annotation.CurrentUser
import io.openfuture.api.component.listener.AddTransactionEventListener
import io.openfuture.api.component.web3.event.ProcessorEventDecoder
import io.openfuture.api.domain.PageRequest
import io.openfuture.api.domain.PageResponse
import io.openfuture.api.domain.scaffold.TransactionDto
import io.openfuture.api.entity.auth.User
import io.openfuture.api.service.ScaffoldService
import io.openfuture.api.service.TransactionService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.async.DeferredResult

@RestController
@RequestMapping("/api/scaffolds/{address}/transactions")
class TransactionApiController(
        private val service: TransactionService,
        private val scaffoldService: ScaffoldService,
        private val eventDecoder: ProcessorEventDecoder,
        private val listener: AddTransactionEventListener
) {

    @GetMapping
    fun getAll(@CurrentUser user: User, @PathVariable address: String,
               pageRequest: PageRequest): PageResponse<TransactionDto> {
        val scaffold = scaffoldService.get(address, user)
        val transactions = service.getAll(scaffold, pageRequest).map { TransactionDto(it,
                eventDecoder.getEvent(it.scaffold.address, it.data)) }
        return PageResponse(transactions)
    }

    @GetMapping("/updates")
    fun getUpdates(@PathVariable address: String): DeferredResult<PageResponse<TransactionDto>> =
            listener.getResult(address)

}