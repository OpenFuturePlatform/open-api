package io.openfuture.api.util

import io.openfuture.api.entity.scaffold.PropertyType
import io.openfuture.api.entity.scaffold.PropertyType.STRING
import org.assertj.core.api.Assertions
import org.junit.Test

class DictionaryUtilsTests {

    @Test
    fun valueOfShouldReturnEnum() {
        val status = DictionaryUtils.valueOf(PropertyType::class.java, STRING.getId())
        Assertions.assertThat(status).isEqualTo(STRING)
    }

    @Test(expected = IllegalStateException::class)
    fun valueOfShouldThrowException() {
        DictionaryUtils.valueOf(PropertyType::class.java, -1)
    }

}
