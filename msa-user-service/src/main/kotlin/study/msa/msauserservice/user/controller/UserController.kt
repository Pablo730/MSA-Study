package study.msa.msauserservice.user.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.core.annotation.Timed
import jakarta.servlet.http.HttpServletRequest
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
@RequestMapping()
class UserController(
    private val env: Environment,
    private val greeting: Greeting,
    private val userService: UserService
) {
    @GetMapping("/health-check")
    @Timed(value="users.status", longTask = true)
    fun status(): String {
        return String.format("It's Working in User Service"
                + ", port(local.server.port): ${env.getProperty("local.server.port")}"
                + ", port(server.port): ${env.getProperty("server.port")}"
                + ", welcome message: ${env.getProperty("greeting.message")}"
                + ", gateway ip(env): ${env.getProperty("gateway.ip")}"
                + ", token secret key: ${env.getProperty("token.secret")}"
                + ", token expiration time: ${env.getProperty("token.expiration-time")}");
    }

    @GetMapping("/welcome")
    @Timed(value="users.welcome", longTask = true)
    fun welcome(request: HttpServletRequest): String {
        logger.info { "users.welcome ip: ${request.remoteAddr}, ${request.remoteHost}, ${request.requestURI}, ${request.requestURL}" }
        return greeting.message
    }

    @PostMapping("/users")
    fun createUser(@RequestBody requestUser: RequestUser): ResponseEntity<ResponseUser> {
        val userDto = userService.createUser(requestUser.toCreateUserDtoWithValidation())
        val responseUser = ResponseUser.fromUserDto(userDto)

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser)
    }

    @GetMapping("/users")
    fun getUsers(): ResponseEntity<List<ResponseUser>> {
        val userList: Iterable<UserDto> = userService.getUserByAll()
        val result: List<ResponseUser> = userList.map { userDto: UserDto -> ResponseUser.fromUserDto(userDto) }

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/users/{userId}")
    fun getUser(@PathVariable("userId") userId: String): ResponseEntity<ResponseUserDetail> {
        val userDetailDto = userService.getUserByUserId(userId)

        return ResponseEntity.ok().body(ResponseUserDetail.fromUserDetailDto(userDetailDto))
    }
}
