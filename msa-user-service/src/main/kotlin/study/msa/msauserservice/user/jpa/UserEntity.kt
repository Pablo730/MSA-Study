package study.msa.msauserservice.user.jpa

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "users")
class UserEntity (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 50, unique = true)
    val email: String,

    @Column(nullable = false, length = 50)
    val name: String,

    @Column(nullable = false, unique = true)
    val userId: String,

    @Column(nullable = false, unique = true)
    val encryptedPwd: String,

    @CreatedDate
    @Column(updatable = false, nullable = false)
    val createdAt: Date? = null,

    @LastModifiedDate
    @Column(nullable = false)
    val updatedAt: Date? = null
)
