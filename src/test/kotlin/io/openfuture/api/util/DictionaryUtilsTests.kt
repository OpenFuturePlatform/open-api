package io.openfuture.api.util

import io.openfuture.api.entity.scaffold.PropertyType
import io.openfuture.api.entity.scaffold.PropertyType.STRING
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class DictionaryUtilsTests {

    @Test
    fun valueOfShouldReturnEnumTest() {
        val status = DictionaryUtils.valueOf(PropertyType::class.java, STRING.getId())

        assertThat(status).isEqualTo(STRING)
    }

    @Test(expected = IllegalStateException::class)
    fun valueOfShouldThrowExceptionTest() {
        DictionaryUtils.valueOf(PropertyType::class.java, -1)
    }

}
