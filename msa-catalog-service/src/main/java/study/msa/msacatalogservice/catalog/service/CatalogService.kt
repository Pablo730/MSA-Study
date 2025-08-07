package study.msa.msacatalogservice.catalog.service

import org.springframework.stereotype.Service
import study.msa.msacatalogservice.catalog.jpa.CatalogRepository
import study.msa.msacatalogservice.catalog.service.dto.CatalogDto


@Service
class CatalogService(
    val catalogRepository: CatalogRepository
) {
    fun getAllCatalogs(): Iterable<CatalogDto> {
        return catalogRepository.findAll().map { catalogEntity ->  CatalogDto.fromCatalogEntity(catalogEntity) }
    }
}
