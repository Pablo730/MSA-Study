package study.msa.msaorderservice.order.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import study.msa.msaorderservice.order.controller.dto.request.RequestOrder
import study.msa.msaorderservice.order.controller.dto.response.ResponseOrder
import study.msa.msaorderservice.order.mq.OrderKafkaProducer
import study.msa.msaorderservice.order.service.OrderService
import study.msa.msaorderservice.order.service.dto.OrderDto
import java.util.*

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/order-service")
class OrderController (
    private val env: Environment,
    private val orderService: OrderService,
    private val orderKafkaProducer: OrderKafkaProducer,
){
    @GetMapping("/health-check")
    fun status(): String {
        return "It's Working in Order Service on LOCAL PORT ${env.getProperty("local.server.port")} (SERVER PORT ${env.getProperty("server.port")}), Datasource URL: ${env.getProperty("spring.datasource.url")}"
    }

    @PostMapping("/{userId}/orders")
    fun createOrder(
        @PathVariable("userId") userId: String?,
        @RequestBody requestOrder: RequestOrder
    ): ResponseEntity<ResponseOrder> {
        /* JPA
        logger.info { "Before add orders data" }
        val orderDto = orderService.createOrder(requestOrder.toCreateOrderDtoWithValidation(userId))
        logger.info { "After added orders data" }
         */
        val createOrderDto = requestOrder.toCreateOrderDtoWithValidation(userId)
        val orderId = UUID.randomUUID().toString()
        val totalPrice = createOrderDto.unitPrice * createOrderDto.qty
        val orderEntity = createOrderDto.toEntity(orderId, totalPrice)
        val orderDto = OrderDto.fromEntity(orderEntity)

        orderKafkaProducer.send2("orders", orderDto)

        // Send order data to Kafka topic
        orderKafkaProducer.send("example-catalog-topic", orderDto)
        logger.info { "Order data sent to Kafka topic" }
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseOrder.fromOrderDto(orderDto))
    }

    @GetMapping("/{userId}/orders")
    fun getOrder(@PathVariable("userId") userId: String): ResponseEntity<List<ResponseOrder>> {
        logger.info { "Before retrieve orders data" }
        val orders: Iterable<OrderDto> = orderService.getOrdersByUserId(userId)
        logger.info { "Add retrieved orders data" }

        return ResponseEntity.ok().body(orders.map { ResponseOrder.fromOrderDto(it) })
    }
}
