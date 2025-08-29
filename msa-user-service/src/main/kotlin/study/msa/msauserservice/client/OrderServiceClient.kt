package study.msa.msauserservice.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import study.msa.msauserservice.client.error.FeignErrorDecoder
import study.msa.msauserservice.user.service.dto.OrderDto

@FeignClient(name = "order-service", url = "http://order-service", configuration = [FeignErrorDecoder::class])
interface OrderServiceClient {
    @GetMapping("/order-service/{userId}/orders")
    fun getOrders(@PathVariable userId: String): List<OrderDto>
}
