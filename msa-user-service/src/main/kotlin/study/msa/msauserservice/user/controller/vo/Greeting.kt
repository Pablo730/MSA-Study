package study.msa.msauserservice.user.controller.vo

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class Greeting(
    @Value("\${greeting.message}")
    val message: String
)
