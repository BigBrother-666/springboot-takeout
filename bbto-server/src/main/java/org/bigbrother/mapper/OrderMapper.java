package org.bigbrother.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.bigbrother.dto.OrdersPageQueryDTO;
import org.bigbrother.entity.Orders;

@Mapper
public interface OrderMapper {
    void insert(Orders orders);

    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    @Select("select * from orders where id=#{id}")
    Orders getById(Long id);

    void update(Orders orders);

    /**
     * 根据订单号查询订单
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 根据状态统计订单数量
     */
    @Select("select count(id) from orders where status = #{status}")
    Integer countStatus(Integer status);
}
