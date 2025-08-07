package study.msa.msauserservice.user.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.Date

@Entity
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
    @Column(nullable = false)
    val createdAt: Date,
    @Column(nullable = false)
    val updatedAt: Date
)
