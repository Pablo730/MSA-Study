package study.msa.msauserservice.user.service.dto

import study.msa.msauserservice.user.jpa.UserEntity
import java.util.*

data class CreateUserDto(
    val pwd: String,
    val email: String,
    val name: String
) {
    fun toCreateEntity(userId: String, encryptedPwd: String): UserEntity {
        val nowDate = Date()
        return UserEntity(
            userId = userId,
            encryptedPwd = encryptedPwd,
            email = this.email,
            name = this.name,
            createdAt = nowDate,
            updatedAt = nowDate
        )
    }
}
