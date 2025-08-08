package study.msa.msaorderservice.order.service.dto

import study.msa.msaorderservice.order.jpa.OrderEntity
import java.util.Date

data class OrderDto(
    val productId: String,
    val qty: Int,
    val unitPrice: Int,
    val totalPrice: Int,
    val orderId: String,
    val userId: String,
    val createdAt: Date
) {
    companion object {
        fun fromEntity(orderEntity: OrderEntity): OrderDto {
            return OrderDto(
                productId = orderEntity.productId,
                qty = orderEntity.qty,
                unitPrice = orderEntity.unitPrice,
                totalPrice = orderEntity.totalPrice,
                orderId = orderEntity.orderId,
                userId = orderEntity.userId,
                createdAt = orderEntity.createdAt
            )
        }
    }
}
