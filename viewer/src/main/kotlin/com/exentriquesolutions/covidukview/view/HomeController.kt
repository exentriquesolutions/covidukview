package com.exentriquesolutions.covidukview.view

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController {
    @GetMapping("/")
    suspend fun index() = "index"
}
