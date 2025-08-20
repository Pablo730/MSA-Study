package study.msa.msaorderservice.order.mq.dto

data class Schema (
    val type: String,
    val fields: List<Field>,
    val optional: Boolean,
    val name: String
)
