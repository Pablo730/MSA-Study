package study.msa.msauserservice.user.service.dto

import study.msa.msauserservice.user.jpa.UserEntity
import java.util.Date

class UserDto (
    val userId: String,
    val email: String,
    val name: String,
    val createdAt: Date,
    val updatedAt: Date
) {
    companion object {
        fun fromEntity(userEntity: UserEntity): UserDto {
            return UserDto(
                userId = userEntity.userId,
                email = userEntity.email,
                name = userEntity.name,
                createdAt = userEntity.createdAt!!,
                updatedAt = userEntity.updatedAt!!,
            )
        }
    }
}
