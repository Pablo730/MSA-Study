package study.msa.msaorderservice.order.jpa

import org.springframework.data.repository.CrudRepository

interface OrderRepository : CrudRepository<OrderEntity, Long> {
    fun findByOrderId(orderId: String): OrderEntity?

    fun findByUserId(userId: String): Iterable<OrderEntity>
}
