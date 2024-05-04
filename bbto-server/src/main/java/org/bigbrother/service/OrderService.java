package org.bigbrother.service;

import org.bigbrother.dto.OrdersSubmitDTO;
import org.bigbrother.vo.OrderSubmitVO;

public interface OrderService {

    /**
     * 用户下单
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);
}
