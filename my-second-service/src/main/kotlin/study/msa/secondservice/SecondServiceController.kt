package study.msa.secondservice

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/second-service")
class SecondServiceController(
    private val env: Environment
) {
    @GetMapping("/welcome")
    fun welcome(): String {
        return "Welcome to the Second service."
    }

    @GetMapping("/message")
    fun message(@RequestHeader("s-request") header: String?): String {
        logger.info { header }
        return "Hello World in Second Service."
    }

    @GetMapping("/check")
    fun check(request: HttpServletRequest): String {
        val requestServerPort: Int = request.serverPort
        logger.info { "Server port=$requestServerPort" }

        val localServerPort = env.getProperty("local.server.port")
        return "Hi, there. This is a message from Second Service on PORT $localServerPort"
    }
}
