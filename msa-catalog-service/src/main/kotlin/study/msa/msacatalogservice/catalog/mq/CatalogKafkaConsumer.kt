package study.msa.msacatalogservice.catalog.mq

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import study.msa.msacatalogservice.catalog.jpa.CatalogRepository


private val logger = KotlinLogging.logger {}

@Service
class CatalogKafkaConsumer (
    private val repository: CatalogRepository
) {
    @KafkaListener(topics = ["example-catalog-topic"])
    fun updateQty(kafkaMessage: String) {
        logger.info { "Kafka Message: ->$kafkaMessage" }

        var map: Map<String, Any> = HashMap()
        val mapper = ObjectMapper()
        try {
            map = mapper.readValue(kafkaMessage, object : TypeReference<Map<String, Any>>() {})
        } catch (ex: JsonProcessingException) {
            ex.printStackTrace()
        }

        val entity = repository.findByProductId((map["productId"].toString()))
        if (entity != null) {
            entity.decreaseStock(map["qty"] as Int)
            repository.save(entity)
        }
    }
}
