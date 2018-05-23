package io.zensoft.open.api.component

import freemarker.template.Configuration
import freemarker.template.Template
import io.zensoft.open.api.exception.TemplateProcessingException
import org.springframework.stereotype.Component
import java.io.StringWriter

/**
 * @author Kadach Alexey
 */
@Component
class TemplateProcessor(private val freemarkerConfig: Configuration) {

    fun getContent(body: String, params: Map<String, Any>): String {
        try {
            val template = Template("template", body, freemarkerConfig)
            val out = StringWriter()
            template.process(params, out)
            out.close()

            return out.toString()
        } catch (e: Exception) {
            throw TemplateProcessingException(e.message)
        }
    }

}