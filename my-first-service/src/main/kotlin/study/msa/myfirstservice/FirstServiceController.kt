package study.msa.myfirstservice

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/first-service")
class FirstServiceController(
    private val env: Environment
) {
    @GetMapping("/welcome")
    fun welcome(): String {
        return "Welcome to the First service."
    }

    @GetMapping("/message")
    fun message(@RequestHeader("f-request") header: String?): String {
        logger.info { header }
        return "Hello World in First Service."
    }

    @GetMapping("/check")
    fun check(request: HttpServletRequest): String {
        val requestServerPort: Int = request.serverPort
        logger.info { "Server port=$requestServerPort" }

        val localServerPort = env.getProperty("local.server.port")
        return "Hi, there. This is a message from First Service on PORT $localServerPort"
    }
}
