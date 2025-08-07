package study.msa.msauserservice.user.controller.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import study.msa.msauserservice.user.service.dto.UserDto

class ResponseUser (
    val email: String,
    val name: String,
    val userId: String
) {
    companion object {
        fun fromUserDto(userDto: UserDto): ResponseUser {
            return ResponseUser(
                email = userDto.email,
                name = userDto.name,
                userId = userDto.userId!!
            )
        }
    }
}
