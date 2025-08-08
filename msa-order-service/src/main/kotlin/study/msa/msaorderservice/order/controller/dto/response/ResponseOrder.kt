package study.msa.msaorderservice.order.controller.dto.response

import study.msa.msaorderservice.order.service.dto.OrderDto
import java.util.Date

data class ResponseOrder(
    val productId: String,
    val qty: Int,
    val unitPrice: Int,
    val totalPrice: Int,
    val orderId: String,
    val createdAt: Date
) {
    companion object {
        fun fromOrderDto(orderDto: OrderDto): ResponseOrder {
            return ResponseOrder(
                productId = orderDto.productId,
                qty = orderDto.qty,
                unitPrice = orderDto.unitPrice,
                totalPrice = orderDto.totalPrice,
                createdAt = orderDto.createdAt,
                orderId = orderDto.orderId
            )
        }
    }
}
