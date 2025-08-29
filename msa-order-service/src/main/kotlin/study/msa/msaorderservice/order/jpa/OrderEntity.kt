package study.msa.msaorderservice.order.jpa

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.Date

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name="orders")
class OrderEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 120)
    val productId: String,

    @Column(nullable = false)
    val qty: Int,

    @Column(nullable = false)
    val unitPrice: Int,

    @Column(nullable = false)
    val totalPrice: Int,

    @Column(nullable = false)
    val userId: String,

    @Column(nullable = false, unique = true)
    val orderId: String,

    @CreatedDate
    @Column(updatable = false, nullable = false)
    val createdAt: Date,

    @LastModifiedDate
    @Column(nullable = false)
    val updatedAt: Date,
)
