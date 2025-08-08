package study.msa.msauserservice.security.dto

import org.apache.coyote.BadRequestException

class RequestLogin(
    val email: String?,
    val password: String?
) {
    fun validate() {
        if (email.isNullOrBlank()) {
            throw BadRequestException("Email cannot be null or blank")
        }
        if (email.length < 2) {
            throw BadRequestException("Email must be at least 2 characters long")
        }
        if (password.isNullOrBlank()) {
            throw BadRequestException("Password cannot be null or blank")
        }
        if (password.length < 8) {
            throw BadRequestException("Password must be at least 8 characters long")
        }
    }
}
