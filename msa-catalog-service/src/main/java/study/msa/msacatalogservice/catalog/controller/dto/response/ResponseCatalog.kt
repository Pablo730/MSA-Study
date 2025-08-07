package study.msa.msacatalogservice.catalog.controller.dto.response

import study.msa.msacatalogservice.catalog.service.dto.CatalogDto
import java.util.Date

data class ResponseCatalog(
    val productId: String,
    val productName: String,
    val unitPrice: Int,
    val stock: Int,
    val createdAt: Date,
    val updatedAt: Date
) {
    companion object {
        fun fromCatalogDto(catalogDto: CatalogDto): ResponseCatalog {
            return ResponseCatalog(
                productId = catalogDto.productId,
                productName = catalogDto.productName,
                unitPrice = catalogDto.unitPrice,
                stock = catalogDto.stock,
                createdAt = catalogDto.createdAt,
                updatedAt = catalogDto.updatedAt
            )
        }
    }
}
