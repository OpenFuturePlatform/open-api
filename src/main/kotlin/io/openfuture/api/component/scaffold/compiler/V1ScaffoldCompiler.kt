package io.openfuture.api.component.scaffold.compiler

import io.openfuture.api.component.template.TemplateProcessor
import io.openfuture.api.config.propety.EthereumProperties
import io.openfuture.api.entity.scaffold.ScaffoldVersion.V1
import org.springframework.stereotype.Component

@Component
class V1ScaffoldCompiler(
        templateProcessor: TemplateProcessor,
        properties: EthereumProperties
) : BaseScaffoldCompiler(V1, templateProcessor, properties)