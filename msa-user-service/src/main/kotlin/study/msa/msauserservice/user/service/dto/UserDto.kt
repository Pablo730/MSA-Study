package study.msa.msauserservice.user.service.dto

import study.msa.msauserservice.user.jpa.UserEntity
import java.util.Date
import java.util.UUID

class UserDto (
    val userId: String? = null,
    val pwd: String? = null,
    val email: String,
    val name: String,
    val createdAt: Date? = null,
    val updatedAt: Date? = null
) {
    fun toCreateEntity(encryptedPwd: String): UserEntity {
        return UserEntity(
            userId = UUID.randomUUID().toString(),
            encryptedPwd = encryptedPwd,
            email = this.email,
            name = this.name,
            createdAt = Date(),
            updatedAt = Date()
        )
    }
    companion object {
        fun fromEntity(userEntity: UserEntity): UserDto {
            return UserDto(
                userId = userEntity.userId,
                email = userEntity.email,
                name = userEntity.name,
                createdAt = userEntity.createdAt,
                updatedAt = userEntity.updatedAt,
            )
        }
    }
}
