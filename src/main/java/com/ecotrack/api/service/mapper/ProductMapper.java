package com.ecotrack.api.service.mapper;

import com.ecotrack.api.domain.Product;
import com.ecotrack.api.service.dto.ProductDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Mapper for the entity {@link Product} and its DTO called {@link ProductDTO}.
 *
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class ProductMapper {

    public Product toEntity(ProductDTO dto) {
        if (dto == null) {
            return null;
        }

        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setSku(dto.getSku());
        product.setDescription(dto.getDescription());
        product.setCategory(dto.getCategory());
        product.setUnitOfMeasure(dto.getUnitOfMeasure());
        product.setTotalCarbonFootprint(dto.getTotalCarbonFootprint());
        product.setProductCreatedDate(dto.getProductCreatedDate());
        product.setProductLastModifiedDate(dto.getProductLastModifiedDate());

        return product;
    }

    public ProductDTO toDto(Product entity) {
        if (entity == null) {
            return null;
        }

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(entity.getId());
        productDTO.setName(entity.getName());
        productDTO.setSku(entity.getSku());
        productDTO.setDescription(entity.getDescription());
        productDTO.setCategory(entity.getCategory());
        productDTO.setUnitOfMeasure(entity.getUnitOfMeasure());
        productDTO.setTotalCarbonFootprint(entity.getTotalCarbonFootprint());
        productDTO.setProductCreatedDate(entity.getProductCreatedDate());
        productDTO.setProductLastModifiedDate(entity.getProductLastModifiedDate());

        return productDTO;
    }

    public void partialUpdate(Product entity, ProductDTO dto) {
        if (dto == null) {
            return;
        }

        if (dto.getId() != null) {
            entity.setId(dto.getId());
        }
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getSku() != null) {
            entity.setSku(dto.getSku());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        if (dto.getCategory() != null) {
            entity.setCategory(dto.getCategory());
        }
        if (dto.getUnitOfMeasure() != null) {
            entity.setUnitOfMeasure(dto.getUnitOfMeasure());
        }
        if (dto.getTotalCarbonFootprint() != null) {
            entity.setTotalCarbonFootprint(dto.getTotalCarbonFootprint());
        }
        if (dto.getProductCreatedDate() != null) {
            entity.setProductCreatedDate(dto.getProductCreatedDate());
        }
        if (dto.getProductLastModifiedDate() != null) {
            entity.setProductLastModifiedDate(dto.getProductLastModifiedDate());
        }
    }

    public Set<ProductDTO> toDtos(Set<Product> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toSet());
    }
}
