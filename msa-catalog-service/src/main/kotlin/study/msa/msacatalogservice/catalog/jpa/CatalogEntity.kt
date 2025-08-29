package study.msa.msacatalogservice.catalog.jpa

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.Date

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "catalog")
class CatalogEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(nullable = false, length = 120, unique = true)
    val productId: String,

    @Column(nullable = false)
    val productName: String,

    @Column(nullable = false)
    var stock: Int,

    @Column(nullable = false)
    val unitPrice: Int,

    @CreatedDate
    @Column(updatable = false, nullable = false)
    val createdAt: Date? = null,

    @LastModifiedDate
    @Column(nullable = false)
    val updatedAt: Date? = null
) {
    fun decreaseStock(stock: Int) {
        this.stock -= stock
    }

    override fun toString(): String {
        return "CatalogEntity(id=$id, productId='$productId', productName='$productName', stock=$stock, unitPrice=$unitPrice, createdAt=$createdAt, updatedAt=$updatedAt)"
    }
}
