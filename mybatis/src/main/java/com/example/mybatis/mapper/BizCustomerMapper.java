package com.example.mybatis.mapper;

import com.example.mybatis.entity.BizCustomer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BizCustomerMapper {

    /**
     * 根据客户 ID 查询客户对象及关联的订单集合（对多的触发方）
     *
     * @param customerId 客户 ID
     * @return 客户对象
     */
    BizCustomer selectCustomerWithOrders(Long customerId);

    /**
     * 根据客户 ID,查询客户对象
     *
     * @param customerId 客户 ID
     * @return 客户对象
     */
    BizCustomer selectCustomerById(Long customerId);

    /**
     * 查询所有客户信息（对多的发起方）
     *
     * @return 所有客户的集合
     */
    List<BizCustomer> selectCustomerList();
}
