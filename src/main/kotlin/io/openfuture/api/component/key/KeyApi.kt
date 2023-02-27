package io.openfuture.api.component.key

import io.openfuture.api.domain.key.*
import java.util.*

interface KeyApi {
    fun generateWallet(createKeyRequest: CreateKeyRequest): KeyWalletDto
    fun importWallet(request: ImportKeyRequest): KeyWalletDto
    fun generateMultipleWallets(createMultipleKeyRequest: CreateMultipleKeyRequest): Array<KeyWalletDto>
    fun updateWallets(createMultipleKeyRequest: CreateMultipleKeyRequest): Array<KeyWalletDto>
    fun getAllWalletsByApplication(applicationId: String, userId: Optional<String>, orderKey: Optional<String>): Array<KeyWalletDto>
    fun getAllWalletsByOrderKey(orderKey: String): Array<KeyWalletDto>
    fun getApplicationByAddress(address: String): WalletAddressResponse
    fun deleteAllWalletsByApplicationAddress(applicationId: String, address: String)
    fun exportPrivateKey(keyWalletDto: KeyWalletDto): String
}