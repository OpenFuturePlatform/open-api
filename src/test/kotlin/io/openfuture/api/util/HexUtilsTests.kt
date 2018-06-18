package io.openfuture.api.util

import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test

class HexUtilsTests {

    @Test
    fun decode() {
        val hex = "0xtest"

        val value = HexUtils.decode(hex)

        assertThat(value).isEqualTo(hex.substring(2))
    }

    @Test
    fun encode() {
        val value = "test"

        val hex = HexUtils.encode(value)

        assertThat(hex).isEqualTo("0x$value")
    }

}
