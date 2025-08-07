package study.msa.msacatalogservice.catalog.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.ColumnDefault
import java.util.Date

@Entity
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

    @Column(nullable = false, updatable = false, insertable = false)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    val createdAt: Date? = null,

    @Column(nullable = false, updatable = true, insertable = false)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    val updatedAt: Date? = null
)
