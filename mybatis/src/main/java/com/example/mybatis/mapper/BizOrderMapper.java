package com.example.mybatis.mapper;

import com.example.mybatis.entity.BizOrder;

import java.util.List;

public interface BizOrderMapper {

    /**
     * 根据订单 ID 查询订单和订单关联的用户信息
     *
     * @param orderId 订单 ID
     * @return Order 订单对象
     */
    BizOrder selectOrderWithCustomer(Long orderId);

    /**
     * 根据 orderId 查询订单和订单关联的客户信息
     *
     * @param orderId 订单 ID
     * @return Order 对象
     */
    BizOrder selectOrderById(Long orderId);

    /**
     * 根据客户 ID 查询对应的订单集合（对多的触发方）
     *
     * @param customerId 顾客 ID
     * @return 查询到的 Order 对象
     */
    List<BizOrder> selectOrderListByCustomerId(Long customerId);
}
