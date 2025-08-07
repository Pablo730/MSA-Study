package study.msa.msauserservice.user.service.dto

import java.util.Date

class OrderDto(
    val productId: String,
    val qty: Int,
    val unitPrice: Int,
    val totalPrice: Int,
    val createdAt: Date,
    val orderId: String
)
