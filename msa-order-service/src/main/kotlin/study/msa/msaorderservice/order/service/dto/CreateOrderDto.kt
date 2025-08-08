package study.msa.msaorderservice.order.service.dto

import study.msa.msaorderservice.order.jpa.OrderEntity
import java.util.Date

data class CreateOrderDto(
    val userId: String,
    val productId: String,
    val qty: Int,
    val unitPrice: Int,
) {
    fun toEntity(orderId: String, totalPrice: Int): OrderEntity {
        return OrderEntity(
            productId = productId,
            qty = qty,
            unitPrice = unitPrice,
            totalPrice = totalPrice,
            userId = userId,
            orderId = orderId,
            createdAt = Date(),
            updatedAt = Date()
        )
    }
}
