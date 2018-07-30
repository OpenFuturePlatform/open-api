package io.openfuture.api.util

import org.web3j.crypto.Keys

/**
 * @author Igor Pahomov
 */
object EthereumUtils {

    private const val NULL_ADDRESS = "0x0000000000000000000000000000000000000000"
    private const val ADDRESS_REGEX = "^0x[0-9a-fA-F]{40}\$"


    fun isValidChecksumAddress(address: String): Boolean =
            isValidAddress(address) && !isAllOneCase(address) && (toChecksumAddress(address) == address)

    private fun toChecksumAddress(address: String): String = Keys.toChecksumAddress(address)

    private fun isValidAddress(address: String): Boolean {
        if (NULL_ADDRESS == address) return false
        return address.matches(Regex(ADDRESS_REGEX))
    }

    private fun isAllOneCase(address: String): Boolean {
        val lowerCase = address.toLowerCase()
        val upperCase = address.toUpperCase()
        return address == lowerCase || address == upperCase
    }

}