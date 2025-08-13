package study.msa.msacatalogservice.catalog.jpa

import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
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
    val stock: Int,

    @Column(nullable = false)
    val unitPrice: Int,

    @CreatedDate
    @Column(updatable = false, nullable = false)
    val createdAt: Date? = null,

    @LastModifiedDate
    @Column(nullable = false)
    val updatedAt: Date? = null
)
