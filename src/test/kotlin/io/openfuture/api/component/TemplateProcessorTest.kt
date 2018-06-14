package io.openfuture.api.component

import freemarker.template.Configuration
import io.openfuture.api.UnitTest
import io.openfuture.api.exception.TemplateProcessingException
import org.apache.commons.lang3.StringUtils
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import java.util.*

/**
 * @author Alexey Skadorva
 */
internal class TemplateProcessorTest : UnitTest() {

    @Mock private lateinit var freemarkerConfig: Configuration

    private lateinit var templateProcessor: TemplateProcessor


    @Before
    fun setUp() {
        templateProcessor = TemplateProcessor(freemarkerConfig)
    }

    @Test(expected = TemplateProcessingException::class)
    fun getContentWithIncorrectBody() {
        templateProcessor.getContent(StringUtils.EMPTY, Collections.emptyMap())
    }

}