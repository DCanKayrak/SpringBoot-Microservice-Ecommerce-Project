package com.dcankayrak.productservice.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductListResponseDto {
    private String name;
    private BigDecimal price;
    private String imageUrl;
    private String slug;
}
