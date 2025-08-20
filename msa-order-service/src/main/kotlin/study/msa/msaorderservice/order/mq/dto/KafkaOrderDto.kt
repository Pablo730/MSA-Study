package study.msa.msaorderservice.order.mq.dto

import java.io.Serializable


data class KafkaOrderDto (val schema: Schema, val payload: Payload) : Serializable
