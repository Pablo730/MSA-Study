package study.msa.msauserservice.user.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import org.apache.coyote.BadRequestException
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import study.msa.msauserservice.user.controller.dto.request.RequestUser
import study.msa.msauserservice.user.controller.dto.response.ResponseUser
import study.msa.msauserservice.user.controller.dto.response.ResponseUserDetail
import study.msa.msauserservice.user.controller.vo.Greeting
import study.msa.msauserservice.user.service.UserService
import study.msa.msauserservice.user.service.dto.UserDto

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("user-service")
class UserController(
    val env: Environment,
    val greeting: Greeting,
    val userService: UserService
) {
    @GetMapping("/health-check")
    fun status(): String {
        val port = env.getProperty("local.server.port")
        val serverPort = env.getProperty("server.port")
        return "It's Working in User Service, port(local.server.port)= $port, port(server.port)= $serverPort"
    }

    @GetMapping("/welcome")
    fun welcome(request: HttpServletRequest): String {
        val addr = request.remoteAddr
        val host = request.remoteHost
        val uri = request.requestURI
        val url = request.requestURL
        logger.info { "users.welcome ip: $addr, $host, $uri, $url" }
        return greeting.message
    }

    @PostMapping("/users")
    fun createUser(@RequestBody requestUser: RequestUser): ResponseEntity<ResponseUser> {
        val createdUserDto = userService.createUser(requestUser.toUserDtoWithValidation())
        val responseUser = ResponseUser.fromUserDto(createdUserDto)

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser)
    }

    @GetMapping("/users")
    fun getUsers(): ResponseEntity<List<ResponseUser>> {
        val userList: Iterable<UserDto> = userService.getUserByAll()
        val result: List<ResponseUser> = userList.map { userDto: UserDto -> ResponseUser.fromUserDto(userDto) }

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/users/{userId}")
    fun getUser(@PathVariable("userId") userId: String?): ResponseEntity<ResponseUserDetail> {
        if (userId.isNullOrBlank()) {
            throw BadRequestException("User ID must not be null or blank")
        }
        val userDetailDto = userService.getUserByUserId(userId)

        return ResponseEntity.ok().body(ResponseUserDetail.fromUserDetailDto(userDetailDto))
    }
}
