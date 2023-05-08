package io.openfuture.api.component.key

import io.openfuture.api.domain.key.*
import io.openfuture.keymanagementservice.dto.GenerateMultipleWalletForOrderRequest
import io.openfuture.keymanagementservice.dto.GenerateMultipleWalletForUserRequest
import io.openfuture.api.domain.key.creation.GenerateMultipleWalletRequest
import java.util.*

interface KeyApi {
    fun generateWallet(createKeyRequest: CreateKeyRequest): KeyWalletDto
    fun importWallet(request: ImportKeyRequest): KeyWalletDto
    fun importWalletV2(request: ImportWalletOpenKeyRequest): KeyWalletDto
    fun generateMultipleWallets(request: GenerateMultipleWalletRequest): Array<KeyWalletDto>
    fun generateMultipleWalletsWithOrder(request: GenerateMultipleWalletForOrderRequest): Array<KeyWalletDto>
    fun generateMultipleWalletsWithUser(request: GenerateMultipleWalletForUserRequest): Array<KeyWalletDto>
    fun updateWallets(createMultipleKeyRequest: CreateMultipleKeyRequest): Array<KeyWalletDto>
    fun getAllWalletsByApplication(applicationId: String, userId: Optional<String>, orderKey: Optional<String>): Array<KeyWalletDto>
    fun getAllWalletsByOrderKey(orderKey: String): Array<KeyWalletDto>
    fun getApplicationByAddress(address: String): WalletAddressResponse
    fun deleteAllWalletsByApplicationAddress(applicationId: String, address: String)
    fun exportPrivateKey(keyWalletDto: KeyWalletDto): String
}