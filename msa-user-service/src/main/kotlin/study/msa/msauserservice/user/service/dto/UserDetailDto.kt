package study.msa.msauserservice.user.service.dto

import java.util.*

data class UserDetailDto(
    val userId: String,
    val email: String,
    val name: String,
    val orders: List<OrderDto>,
    val createdAt: Date,
    val updatedAt: Date
) {
    companion object {
        fun fromUserDtoAndOrders(userDto: UserDto, orders: List<OrderDto>): UserDetailDto {
            return UserDetailDto(
                userId = userDto.userId,
                email = userDto.email,
                name = userDto.name,
                orders = orders,
                createdAt = userDto.createdAt,
                updatedAt = userDto.updatedAt
            )
        }
    }
}
