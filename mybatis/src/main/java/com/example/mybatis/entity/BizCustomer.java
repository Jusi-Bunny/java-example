package com.example.mybatis.entity;

import lombok.Data;

import java.util.List;

@Data
public class BizCustomer {

    private Long customerId;

    private String customerName;

    // 体现的是对多的关系
    private List<BizOrder> orders;
}