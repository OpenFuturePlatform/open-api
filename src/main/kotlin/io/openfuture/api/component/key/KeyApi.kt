package io.openfuture.api.component.key

import io.openfuture.api.domain.key.CreateKeyRequest
import io.openfuture.api.domain.key.KeyWalletDto
import io.openfuture.api.domain.key.WalletAddressResponse

interface KeyApi {
    fun generateKey(createKeyRequest: CreateKeyRequest): KeyWalletDto
    fun getAllKeysByApplication(applicationId: String): Array<KeyWalletDto>
    fun getApplicationByAddress(address: String): WalletAddressResponse
    fun deleteAllKeysByApplicationAddress(applicationId: String, address: String)
}