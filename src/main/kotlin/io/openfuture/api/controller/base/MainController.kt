package io.openfuture.api.controller.base

import io.openfuture.api.config.propety.WidgetProperties
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class MainController(
        private val widgetProperties: WidgetProperties
) {

    @GetMapping("/", "/ethereum-scaffold", "/ethereum-scaffolds", "/ethereum-scaffolds/**", "/applications", "/applications/**", "/keys", "/keys/**", "/scaffolds/**")
    fun frontend() = "frontend"

    @GetMapping("/widget/{address}")
    fun widget(@PathVariable address: String, model: ModelMap): String {
        model["address"] = address
        model["host"] = widgetProperties.host
        return "widget"
    }

}