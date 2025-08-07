package study.msa.msauserservice.user.controller.dto.request

import org.apache.coyote.BadRequestException
import study.msa.msauserservice.user.service.dto.CreateUserDto

data class RequestUser (
    private val email: String? = null,
    private val pwd: String? = null,
    private val name: String? = null
) {
    fun toCreateUserDtoWithValidation(): CreateUserDto {
        if (email.isNullOrBlank()) {
            throw BadRequestException("Email must not be null")
        }
        if (pwd.isNullOrBlank()) {
            throw BadRequestException("Password must not be null")
        }
        if (name.isNullOrBlank()) {
            throw BadRequestException("Name must not be null")
        }
        return CreateUserDto(
            email = this.email,
            pwd = this.pwd,
            name = this.name,
        )
    }
}
