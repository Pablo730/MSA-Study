package study.msa.msaorderservice.order.service

import org.apache.coyote.BadRequestException
import org.springframework.stereotype.Service
import study.msa.msaorderservice.order.jpa.OrderRepository
import study.msa.msaorderservice.order.service.dto.CreateOrderDto
import study.msa.msaorderservice.order.service.dto.OrderDto
import java.util.*


@Service
class OrderService (
    val orderRepository: OrderRepository
) {
    fun createOrder(createOrderDto: CreateOrderDto): OrderDto {
        val orderId = UUID.randomUUID().toString()
        val totalPrice = createOrderDto.unitPrice * createOrderDto.qty
        val orderEntity = orderRepository.save(createOrderDto.toEntity(orderId, totalPrice))

        return OrderDto.fromEntity(orderEntity)
    }

    fun getOrderByOrderId(orderId: String): OrderDto {
        val orderEntity = orderRepository.findByOrderId(orderId) ?: throw BadRequestException("Order not found with orderId: $orderId")

        return OrderDto.fromEntity(orderEntity)
    }

    fun getOrdersByUserId(userId: String): Iterable<OrderDto> {
        return orderRepository.findByUserId(userId).map { OrderDto.fromEntity(it) }
    }
}
