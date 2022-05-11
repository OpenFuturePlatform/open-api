package io.openfuture.api.component.key

import io.openfuture.api.domain.key.*

interface KeyApi {
    fun generateKey(createKeyRequest: CreateKeyRequest): KeyWalletDto
    fun importWallet(request: ImportKeyRequest): KeyWalletDto
    fun generateMultipleKey(createMultipleKeyRequest: CreateMultipleKeyRequest): Array<KeyWalletDto>
    fun getAllKeysByApplication(applicationId: String): Array<KeyWalletDto>
    fun getAllKeysByOrderKey(orderKey: String): Array<KeyWalletDto>
    fun getApplicationByAddress(address: String): WalletAddressResponse
    fun deleteAllKeysByApplicationAddress(applicationId: String, address: String)
    fun exportPrivateKey(keyWalletDto: KeyWalletDto): String
}