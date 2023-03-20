package io.openfuture.api.entity.state

import com.fasterxml.jackson.annotation.JsonValue

enum class Blockchain(private val value: String) {

    Ethereum("EthereumBlockchain"),
    Goerli("GoerliBlockchain"),
    Bitcoin("BitcoinBlockchain"),
    Binance("BinanceBlockchain"),
    BinanceTestnet("BinanceTestnetBlockchain");

    companion object {
        //TODO
        fun getBlockchainBySymbol(symbol: String): Blockchain {
            return when (symbol) {
                "ETH" -> Goerli
                "BNB" -> BinanceTestnet
                "BTC" -> Bitcoin
                else -> Goerli
            }
        }
    }

    @JsonValue
    fun getValue(): String = value

}
