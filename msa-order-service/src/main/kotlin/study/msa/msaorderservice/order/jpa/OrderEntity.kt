package study.msa.msaorderservice.order.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.ColumnDefault
import java.util.Date

@Entity
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

    @Column(nullable = false, updatable = false, insertable = false)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    val createdAt: Date,

    @Column(nullable = false, updatable = true, insertable = false)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    val updatedAt: Date,
)
