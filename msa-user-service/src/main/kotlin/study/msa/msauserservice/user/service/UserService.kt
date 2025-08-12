package study.msa.msauserservice.user.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.coyote.BadRequestException
import org.springframework.core.ParameterizedTypeReference
import org.springframework.core.env.Environment
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import study.msa.msauserservice.client.OrderServiceClient
import study.msa.msauserservice.user.jpa.UserEntity
import study.msa.msauserservice.user.jpa.UserRepository
import study.msa.msauserservice.user.service.dto.CreateUserDto
import study.msa.msauserservice.user.service.dto.OrderDto
import study.msa.msauserservice.user.service.dto.UserDetailDto
import study.msa.msauserservice.user.service.dto.UserDto
import java.util.*

private val logger = KotlinLogging.logger {}

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val restTemplate: RestTemplate,
    private val orderServiceClient: OrderServiceClient,
    private val env: Environment
): UserDetailsService {
    fun createUser(createUserDto: CreateUserDto): UserDto {
        val randomUserId = UUID.randomUUID().toString()
        val cryptoPwd = passwordEncoder.encode(createUserDto.pwd)
        val userEntity = userRepository.save(createUserDto.toCreateEntity(randomUserId, cryptoPwd))
        logger.info { "Created user id: ${userEntity.id}" }

        return UserDto.fromEntity(userEntity)
    }

    fun getUserByAll(): Iterable<UserDto> {
        val users = userRepository.findAll()

        return users.map { user -> UserDto.fromEntity(user) }
    }

    fun getUserByUserId(userId: String): UserDetailDto {
        val userEntity = userRepository.findByUserId(userId) ?: throw BadRequestException("User not found with userId: $userId")
        val userDto = UserDto.fromEntity(userEntity)
        /*
        // RestTemplate 사용한 주문 서비스 호출 예시
        val orderUrl: String = String.format(env.getProperty("order-service.url").toString(), userId)
        val responseOrders: ResponseEntity<List<OrderDto>> =
            restTemplate.exchange(
                orderUrl, HttpMethod.GET,
                null,
                object : ParameterizedTypeReference<List<OrderDto>>() {}
            )
        val orders: List<OrderDto> = responseOrders.body ?: ArrayList()
         */
        // FeignClient 사용한 주문 서비스 호출 예시
        val orders: List<OrderDto> = orderServiceClient.getOrders(userId)

        return UserDetailDto.fromUserDtoAndOrders(userDto, orders)
    }

    fun getUserDetailsByEmail(userEmail: String): UserDto {
        val userEntity: UserEntity = userRepository.findByEmail(userEmail) ?: throw BadRequestException("User not found with email: $userEmail")

        return UserDto.fromEntity(userEntity)
    }

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val userEntity = userRepository.findByEmail(username) ?: throw UsernameNotFoundException("$username: not found")

        return User(userEntity.email, userEntity.encryptedPwd, true, true, true, true, ArrayList())
    }
}
