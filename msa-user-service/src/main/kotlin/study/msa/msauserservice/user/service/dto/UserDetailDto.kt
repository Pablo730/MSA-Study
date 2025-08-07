package study.msa.msauserservice.user.service.dto

import java.util.*

data class UserDetailDto(
    val userId: String,
    val pwd: String,
    val email: String,
    val name: String,
    val orders: List<OrderDto>,
    val createdAt: Date,
    val updatedAt: Date
) {
    companion object {
        fun fromUserDtoAndOrders(userDto: UserDto, orders: List<OrderDto>): UserDetailDto {
            if (userDto.userId.isNullOrBlank() || userDto.createdAt == null || userDto.updatedAt == null) {
                throw IllegalArgumentException("UserDto must have non-null userId, createdAt, and updatedAt")
            }
            return UserDetailDto(
                userId = userDto.userId,
                pwd = userDto.pwd,
                email = userDto.email,
                name = userDto.name,
                orders = orders,
                createdAt = userDto.createdAt,
                updatedAt = userDto.updatedAt
            )
        }
    }
}
