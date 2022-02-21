package io.openfuture.api.controller.widget

import io.openfuture.api.domain.state.StateWalletTransactionDetail
import io.openfuture.api.service.ApplicationWalletService
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/widget/transactions")
class TransactionWidgetController(
    private val service : ApplicationWalletService
) {
    @GetMapping("/{address}")
    fun getTransactionsByAddress(@PathVariable address: String): StateWalletTransactionDetail {
        return service.getAddressTransactionsByAddress(address)
    }

    @GetMapping("/order-key/{orderKey}")
    fun getTransactionsByOrder(@PathVariable orderKey: String): StateWalletTransactionDetail {
        return service.getAddressTransactionsByOrder(orderKey)
    }
}