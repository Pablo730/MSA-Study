package study.msa.msaorderservice.order.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.coyote.BadRequestException
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import study.msa.msaorderservice.order.controller.dto.request.RequestOrder
import study.msa.msaorderservice.order.controller.dto.response.ResponseOrder
import study.msa.msaorderservice.order.service.OrderService
import study.msa.msaorderservice.order.service.dto.OrderDto

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/order-service")
class OrderController (
    val env: Environment,
    val orderService: OrderService
){
    @GetMapping("/health-check")
    fun status(): String {
        return "It's Working in Order Service on LOCAL PORT ${env.getProperty("local.server.port")} (SERVER PORT ${env.getProperty("server.port")})"
    }

    @PostMapping("/{userId}/orders")
    fun createOrder(
        @PathVariable("userId") userId: String?,
        @RequestBody requestOrder: RequestOrder
    ): ResponseEntity<ResponseOrder> {
        val createOrderDto = requestOrder.toCreateOrderDtoWithValidation(userId)
        logger.info { "Before add orders data" }
        val orderDto = orderService.createOrder(createOrderDto)
        logger.info { "After added orders data" }

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseOrder.fromOrderDto(orderDto))
    }

    @GetMapping("/{userId}/orders")
    fun getOrder(@PathVariable("userId") userId: String?): ResponseEntity<List<ResponseOrder>> {
        if (userId.isNullOrBlank()) {
            throw BadRequestException("User ID must not be null or blank")
        }
        logger.info { "Before retrieve orders data" }
        val orders: Iterable<OrderDto> = orderService.getOrdersByUserId(userId)
        logger.info { "Add retrieved orders data" }

        return ResponseEntity.ok().body(orders.map { ResponseOrder.fromOrderDto(it) })
    }
}
