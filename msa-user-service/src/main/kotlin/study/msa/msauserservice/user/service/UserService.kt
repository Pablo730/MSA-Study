package study.msa.msauserservice.user.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import study.msa.msauserservice.user.jpa.UserRepository
import study.msa.msauserservice.user.service.dto.UserDto

private val logger = KotlinLogging.logger {}

@Service
class UserService(
    val userRepository: UserRepository,
    val passwordEncoder: BCryptPasswordEncoder
) {
    fun createUser(userDto: UserDto): UserDto {
        val userEntity = userDto.toCreateEntity(passwordEncoder.encode(userDto.pwd))
        userRepository.save(userEntity)
        val userId = userEntity.id ?: throw IllegalStateException("User ID should not be null after saving")
        logger.info { "Created user id: $userId" }

        return UserDto.fromEntity(userEntity)
    }
}
