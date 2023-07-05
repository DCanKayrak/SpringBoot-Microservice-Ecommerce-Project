package com.dcankayrak.productservice.entity;

import com.dcankayrak.productservice.dto.response.UserResponseDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order extends BaseEntity{
    private String orderNumber;
}
