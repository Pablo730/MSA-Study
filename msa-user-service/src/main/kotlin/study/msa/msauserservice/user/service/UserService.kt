package study.msa.msauserservice.user.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.coyote.BadRequestException
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import study.msa.msauserservice.user.jpa.UserEntity
import study.msa.msauserservice.user.jpa.UserRepository
import study.msa.msauserservice.user.service.dto.OrderDto
import study.msa.msauserservice.user.service.dto.UserDetailDto
import study.msa.msauserservice.user.service.dto.UserDto
import java.util.*
import kotlin.collections.ArrayList


private val logger = KotlinLogging.logger {}

@Service
class UserService(
    val userRepository: UserRepository,
    val passwordEncoder: BCryptPasswordEncoder
) {
    fun createUser(userDto: UserDto): UserDto {
        val randomUserId = UUID.randomUUID().toString()
        val cryptoPwd = passwordEncoder.encode(userDto.pwd)
        val userEntity = userRepository.save(userDto.toCreateEntity(randomUserId, cryptoPwd))
        val createdEntityId = userEntity.id ?: throw IllegalStateException("Entity ID should not be null after saving")
        logger.info { "Created user id: $createdEntityId" }

        return UserDto.fromEntity(userEntity)
    }

    fun getUserByAll(): Iterable<UserDto> {
        val users = userRepository.findAll()

        return users.map { user -> UserDto.fromEntity(user) }
    }

    fun getUserByUserId(userId: String): UserDetailDto {
        val userEntity: UserEntity = userRepository.findByUserId(userId) ?: throw BadRequestException("User not found with userId: $userId")
        val userDto: UserDto = UserDto.fromEntity(userEntity)
        val orders: List<OrderDto> = ArrayList()

        return UserDetailDto.fromUserDtoAndOrders(userDto, orders)
    }
}
