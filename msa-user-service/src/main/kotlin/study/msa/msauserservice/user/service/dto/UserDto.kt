package study.msa.msauserservice.user.service.dto

import study.msa.msauserservice.user.jpa.UserEntity
import java.util.Date

class UserDto (
    val userId: String? = null,
    val pwd: String,
    val email: String,
    val name: String,
    val createdAt: Date? = null,
    val updatedAt: Date? = null
) {
    fun toCreateEntity(userId: String, encryptedPwd: String): UserEntity {
        return UserEntity(
            userId = userId,
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
                pwd = userEntity.encryptedPwd,
                email = userEntity.email,
                name = userEntity.name,
                createdAt = userEntity.createdAt,
                updatedAt = userEntity.updatedAt,
            )
        }
    }
}
