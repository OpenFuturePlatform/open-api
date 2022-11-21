package io.openfuture.api.component.key

import io.openfuture.api.domain.key.*

interface KeyApi {
    fun generateWallet(createKeyRequest: CreateKeyRequest): KeyWalletDto
    fun importWallet(request: ImportKeyRequest): KeyWalletDto
    fun generateMultipleWallets(createMultipleKeyRequest: CreateMultipleKeyRequest): Array<KeyWalletDto>
    fun updateWallets(createMultipleKeyRequest: CreateMultipleKeyRequest): Array<KeyWalletDto>
    fun getAllWalletsByApplication(applicationId: String): Array<KeyWalletDto>
    fun getAllWalletsByOrderKey(orderKey: String): Array<KeyWalletDto>
    fun getApplicationByAddress(address: String): WalletAddressResponse
    fun deleteAllWalletsByApplicationAddress(applicationId: String, address: String)
    fun exportPrivateKey(keyWalletDto: KeyWalletDto): String
}