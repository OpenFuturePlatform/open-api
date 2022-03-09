package io.openfuture.api.controller.widget

import io.openfuture.api.domain.key.KeyWalletDto
import io.openfuture.api.domain.widget.PaymentWidgetResponse
import io.openfuture.api.service.WalletApiService
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/widget/payment/addresses")
class PaymentAddressWidgetController(
    private val service : WalletApiService
) {
    @GetMapping("/order/{orderKey}")
    fun getAllAddressesByOrder(@PathVariable orderKey: String): PaymentWidgetResponse {
        return service.getAddressesByOrderKey(orderKey)
    }
}