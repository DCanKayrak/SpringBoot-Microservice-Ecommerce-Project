package com.dcankayrak.productservice.service;

import com.dcankayrak.productservice.converter.ProductConverter;
import com.dcankayrak.productservice.core.Slugify;
import com.dcankayrak.productservice.dto.request.product.ProductSaveRequestDto;
import com.dcankayrak.productservice.dto.request.product.ProductUpdateRequestDto;
import com.dcankayrak.productservice.dto.response.ProductListResponseDto;
import com.dcankayrak.productservice.entity.Product;
import com.dcankayrak.productservice.exception.ProductNotFoundException;
import com.dcankayrak.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductConverter productConverter;
    private final Slugify slugify;

    @Cacheable(value = "productList",cacheManager = "defaultCacheManager")
    public List<ProductListResponseDto> getProductList() {
        return productConverter.convertProductsToProductListResponseDto(productRepository.findAll());
    }

    @CacheEvict(value = "productList",cacheManager = "defaultCacheManager")
    public List<ProductListResponseDto> getProductListPut() {
        return productConverter.convertProductsToProductListResponseDto(productRepository.findAll());
    }

    public ProductListResponseDto getProductWithSlug(String slug) {
        return productConverter.convertProductToProductListResponseDto(productRepository.findBySlug(slug));
    }

    public void saveProduct(ProductSaveRequestDto request) {
        Product tempProduct = new Product();
        tempProduct.setName(request.getName());
        tempProduct.setPrice(request.getPrice());
        tempProduct.setDescription(request.getDescription());
        tempProduct.setSlug(slugify.slugify(request.getName(),'p'));
        productRepository.save(tempProduct);
    }

    public void deleteProduct(Long productId) {
        Product tempProduct = this.productRepository.findById(productId).orElseThrow(() -> {throw new ProductNotFoundException("Aradığınız ürün numarasına ait bir ürün bulunmamaktadır.");});
        this.productRepository.delete(tempProduct);
    }

    public ProductListResponseDto updateProduct(ProductUpdateRequestDto request, Long id) {
        Product tempProduct = this.productRepository.findById(id).orElseThrow(() -> {throw new ProductNotFoundException("Aradığınız ürün numarasına ait bir ürün bulunmamaktadır.");});
        tempProduct.setName(request.getName());
        tempProduct.setPrice(request.getPrice());
        tempProduct.setDescription(request.getDescription());
        this.productRepository.save(tempProduct);

        ProductListResponseDto tempProductListResponseDto = this.productConverter
                .convertProductToProductListResponseDto(tempProduct);
        return tempProductListResponseDto;
    }
}
