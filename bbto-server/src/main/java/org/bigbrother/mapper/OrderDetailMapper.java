package org.bigbrother.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.bigbrother.entity.OrderDetail;

import java.util.List;

@Mapper
public interface OrderDetailMapper {
    void insertBatch(List<OrderDetail> orderDetailList);
}
