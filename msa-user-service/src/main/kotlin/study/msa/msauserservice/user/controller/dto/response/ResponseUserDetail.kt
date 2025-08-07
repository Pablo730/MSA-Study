package study.msa.msauserservice.user.controller.dto.response

import study.msa.msauserservice.user.service.dto.OrderDto
import study.msa.msauserservice.user.service.dto.UserDetailDto
import java.util.*

data class ResponseUserDetail(
    val userId: String,
    val orders: List<OrderDto>,
    val email: String,
    val name: String,
    val createdAt: Date,
    val updatedAt: Date
) {
    companion object {
        fun fromUserDetailDto(userDetailDto: UserDetailDto): ResponseUserDetail {
            return ResponseUserDetail(
                userId = userDetailDto.userId,
                orders = userDetailDto.orders,
                email = userDetailDto.email,
                name = userDetailDto.name,
                createdAt = userDetailDto.createdAt,
                updatedAt = userDetailDto.updatedAt
            )
        }
    }
}
