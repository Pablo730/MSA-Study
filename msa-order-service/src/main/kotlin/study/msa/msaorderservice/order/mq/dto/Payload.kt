package study.msa.msaorderservice.order.mq.dto


data class Payload (
    val order_id: String,
    val user_id: String,
    val product_id: String,
    val qty: Int,
    val unit_price: Int,
    val total_price: Int
)
