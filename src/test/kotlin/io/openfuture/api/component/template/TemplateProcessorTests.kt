package io.openfuture.api.component.template

import freemarker.template.Configuration
import io.openfuture.api.config.UnitTest
import io.openfuture.api.exception.TemplateProcessingException
import org.apache.commons.lang3.StringUtils.EMPTY
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import java.util.*

internal class TemplateProcessorTests : UnitTest() {

    @Mock private lateinit var freemarkerConfig: Configuration

    private lateinit var templateProcessor: TemplateProcessor


    @Before
    fun setUp() {
        templateProcessor = TemplateProcessor(freemarkerConfig)
    }

    @Test(expected = TemplateProcessingException::class)
    fun getContentWhenIncorrectBodyShouldThrowExceptionTest() {
        templateProcessor.getContent(EMPTY, Collections.emptyMap())
    }

}