package study.msa.msacatalogservice.catalog.controller

import org.springframework.core.env.Environment
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import study.msa.msacatalogservice.catalog.controller.dto.response.ResponseCatalog
import study.msa.msacatalogservice.catalog.service.CatalogService

@RestController
@RequestMapping("/catalog-service")
class CatalogController(
    val env: Environment,
    val catalogService: CatalogService
) {
    @GetMapping("/health-check")
    fun status(): String {
        return "It's Working in Catalog Service, port(local.server.port): ${env.getProperty("local.server.port")}, port(server.port): ${env.getProperty("server.port")}"
    }

    @GetMapping("/catalogs")
    fun getCatalogs(): ResponseEntity<List<ResponseCatalog>> {
        val catalogsDto = catalogService.getAllCatalogs()
        val responseCatalogs = catalogsDto.map { catalogDto -> ResponseCatalog.fromCatalogDto(catalogDto) }

        return ResponseEntity.ok().body(responseCatalogs)
    }
}
