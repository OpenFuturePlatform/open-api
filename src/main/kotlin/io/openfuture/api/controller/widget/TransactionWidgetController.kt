package io.openfuture.api.controller.widget

import io.openfuture.api.domain.transaction.TransactionDto
import io.openfuture.api.service.ApplicationWalletService
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/widget/transactions")
class TransactionWidgetController(
    private val service: ApplicationWalletService
) {

    @GetMapping("/address/{address}")
    fun getTransactionsByAddress(@PathVariable address: String): Array<TransactionDto> {
        return service.getAllTransactionsByAddress(address)
    }
}