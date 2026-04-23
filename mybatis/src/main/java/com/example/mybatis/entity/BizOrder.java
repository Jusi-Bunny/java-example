package com.example.mybatis.entity;

import lombok.Data;

@Data
public class BizOrder {

    private Long orderId;

    private String orderName;

    private Long customerId;

    // 体现的是对一的关系
    private BizCustomer customer;
}