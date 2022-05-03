package io.openfuture.api.controller.widget

import io.openfuture.api.domain.state.StateWalletTransactionDetail
import io.openfuture.api.domain.transaction.TransactionDto
import io.openfuture.api.service.ApplicationWalletService
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/widget/transactions")
class TransactionWidgetController(
    private val service : ApplicationWalletService
) {
    /*@GetMapping("/address/{address}")
    fun getTransactionsByAddress(@PathVariable address: String): StateWalletTransactionDetail {
        return service.getAddressTransactionsByAddress(address)
    }*/

    @GetMapping("/address/{address}")
    fun getTransactionsByAddress(@PathVariable address: String): Array<TransactionDto> {
        return service.getTransactionsByAddress(address)
    }

    /*@GetMapping("/{address}")
    fun getTransactionsOnlyByAddress(@PathVariable address: String): Array<TransactionDto> {
        return service.getTransactionsByAddress(address)
    }*/
}