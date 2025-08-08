package study.msa.msaorderservice.order.controller.dto.request

import org.apache.coyote.BadRequestException
import study.msa.msaorderservice.order.service.dto.CreateOrderDto

data class RequestOrder(
    val productId: String? = null,
    val qty: Int? = null,
    val unitPrice: Int? = null
) {
    fun toCreateOrderDtoWithValidation(userId: String?): CreateOrderDto {
        if (productId.isNullOrBlank()) {
            throw BadRequestException("Product ID must not be null or blank")
        }
        if (qty == null || qty <= 0) {
            throw BadRequestException("Quantity must be a positive integer")
        }
        if (unitPrice == null || unitPrice <= 0) {
            throw BadRequestException("Unit price must be a positive integer")
        }
        if (userId.isNullOrBlank()) {
            throw BadRequestException("User ID must not be null or blank")
        }
        return CreateOrderDto(
            userId = userId,
            productId = productId,
            qty = qty,
            unitPrice = unitPrice
        )
    }
}
