package io.openfuture.api.component.scaffold.compiler

import io.openfuture.api.component.template.TemplateProcessor
import io.openfuture.api.config.propety.EthereumProperties
import io.openfuture.api.entity.scaffold.ScaffoldVersion.V2
import org.springframework.stereotype.Component

@Component
class V2EthereumScaffoldCompiler(
        templateProcessor: TemplateProcessor,
        properties: EthereumProperties
) : EthereumScaffoldCompiler(V2, templateProcessor, properties)