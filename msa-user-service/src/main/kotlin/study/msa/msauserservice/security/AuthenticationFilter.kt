package study.msa.msauserservice.security

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.env.Environment
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import study.msa.msauserservice.security.dto.RequestLogin
import study.msa.msauserservice.user.service.UserService
import study.msa.msauserservice.user.service.dto.UserDto
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.Date
import javax.crypto.SecretKey

class AuthenticationFilter(
    private val userService: UserService,
    private val env: Environment,
    private val authenticationManager: AuthenticationManager
) : UsernamePasswordAuthenticationFilter()
{
    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(
        req: HttpServletRequest,
        res: HttpServletResponse
    ): Authentication {
        try {
            val creds: RequestLogin = jacksonObjectMapper().readValue(req.inputStream)
            creds.validate()

            return authenticationManager.authenticate(UsernamePasswordAuthenticationToken(creds.email, creds.password, ArrayList()))
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(
        req: HttpServletRequest?,
        res: HttpServletResponse,
        chain: FilterChain?,
        authResult: Authentication
    ) {
        val userEmail: String = (authResult.principal as User).username // email
        val userDetails: UserDto =  userService.getUserDetailsByEmail(userEmail)

        val configSecretKey = env.getProperty("token.secret") ?: throw IllegalStateException("Token secret is not configured")
        val secretKeyBytes: ByteArray = configSecretKey.toByteArray(StandardCharsets.UTF_8)
        val secretKey: SecretKey = Keys.hmacShaKeyFor(secretKeyBytes)

        val now = Instant.now()
        val configExpirationTime = env.getProperty("token.expiration-time") ?: throw IllegalStateException("Token expiration time is not configured")
        val token: String = Jwts.builder()
            .subject(userDetails.userId) // UUID
            .expiration(Date.from(now.plusMillis(configExpirationTime.toLong())))
            .issuedAt(Date.from(now))
            .signWith(secretKey) // salt
            .compact()

        res.addHeader("token", token)
        res.addHeader("userId", userDetails.userId)
    }
}
