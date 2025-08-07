package study.msa.msauserservice.user.controller.dto.response

import study.msa.msauserservice.user.service.dto.UserDto
import java.util.Date

data class ResponseUser (
    val email: String,
    val name: String,
    val userId: String,
    val createdAt: Date,
    val updatedAt: Date
) {
    companion object {
        fun fromUserDto(userDto: UserDto): ResponseUser {
            return ResponseUser(
                email = userDto.email,
                name = userDto.name,
                userId = userDto.userId,
                createdAt = userDto.createdAt,
                updatedAt = userDto.updatedAt
            )
        }
    }
}
