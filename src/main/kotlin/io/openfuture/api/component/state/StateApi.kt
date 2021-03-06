package io.openfuture.api.component.state

import io.openfuture.api.domain.state.StateWalletDto
import io.openfuture.api.entity.state.Blockchain

interface StateApi {

    fun createWallet(address: String, webHook: String, blockchain: Blockchain): StateWalletDto

}
