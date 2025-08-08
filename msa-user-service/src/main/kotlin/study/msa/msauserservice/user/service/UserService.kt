package study.msa.msauserservice.user.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.coyote.BadRequestException
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
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
    private val passwordEncoder: BCryptPasswordEncoder
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
        val orders: List<OrderDto> = ArrayList()

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
