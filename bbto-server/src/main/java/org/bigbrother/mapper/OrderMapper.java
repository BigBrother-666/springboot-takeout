package org.bigbrother.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.bigbrother.entity.Orders;

@Mapper
public interface OrderMapper {
    void insert(Orders orders);
}
