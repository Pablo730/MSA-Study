package study.msa.msacatalogservice.catalog.service.dto

import study.msa.msacatalogservice.catalog.jpa.CatalogEntity
import java.util.Date

data class CatalogDto(
    val productId: String,
    val productName: String,
    val stock: Int,
    val unitPrice: Int,
    val totalPrice: Int,
    val orderId: String,
    val userId:String,
    val createdAt: Date,
    val updatedAt: Date,
) {
    companion object {
        fun fromCatalogEntity(catalogEntity: CatalogEntity): CatalogDto {
            return CatalogDto(
                productId = catalogEntity.productId,
                productName = catalogEntity.productName,
                stock = catalogEntity.stock,
                unitPrice = catalogEntity.unitPrice,
                totalPrice = catalogEntity.unitPrice * catalogEntity.stock,
                orderId = "test", //catalogEntity.orderId,
                userId = "test",//catalogEntity.userId,
                createdAt = catalogEntity.createdAt!!,
                updatedAt = catalogEntity.updatedAt!!
            )
        }
    }
}
