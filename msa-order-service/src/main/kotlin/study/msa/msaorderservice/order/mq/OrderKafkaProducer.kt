package study.msa.msaorderservice.order.mq

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import study.msa.msaorderservice.order.mq.dto.Field
import study.msa.msaorderservice.order.mq.dto.KafkaOrderDto
import study.msa.msaorderservice.order.mq.dto.Payload
import study.msa.msaorderservice.order.mq.dto.Schema
import study.msa.msaorderservice.order.service.dto.OrderDto


private val logger = KotlinLogging.logger {}

@Service
class OrderKafkaProducer (
    private val kafkaTemplate: KafkaTemplate<String, String>
) {
    fun send(topic: String, orderDto: OrderDto): OrderDto {
        val mapper = ObjectMapper()
        var jsonInString: String? = ""
        try {
            jsonInString = mapper.writeValueAsString(orderDto)
        } catch (ex: JsonProcessingException) {
            ex.printStackTrace()
        }

        kafkaTemplate.send(topic, jsonInString)
        logger.info { "Kafka Producer sent data from the Order microservice: $jsonInString" }

        return orderDto
    }

    fun send2(topic: String, orderDto: OrderDto): KafkaOrderDto {
        val fields = listOf(
            Field("string", true, "order_id"),
            Field("string", true, "user_id"),
            Field("string", true, "product_id"),
            Field("int32", true, "qty"),
            Field("int32", true, "unit_price"),
            Field("int32", true, "total_price")
        )
        val schema = Schema(type = "struct", fields = fields, optional = false, name = "orders")

        val payload = Payload(
            order_id = orderDto.orderId,
            user_id = orderDto.userId,
            product_id = orderDto.productId,
            qty = orderDto.qty,
            unit_price = orderDto.unitPrice,
            total_price = orderDto.totalPrice)

        val kafkaOrderDto = KafkaOrderDto(schema = schema, payload = payload)

        val mapper = ObjectMapper()
        var jsonInString: String? = ""
        try {
            jsonInString = mapper.writeValueAsString(kafkaOrderDto)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        kafkaTemplate.send(topic, jsonInString)
        logger.info { "Order Producer sent data from the Order microservice: $jsonInString" }

        return kafkaOrderDto
    }
}
