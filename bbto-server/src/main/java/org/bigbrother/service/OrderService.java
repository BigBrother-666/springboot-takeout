package org.bigbrother.service;

import org.bigbrother.dto.*;
import org.bigbrother.result.PageResult;
import org.bigbrother.vo.OrderPaymentVO;
import org.bigbrother.vo.OrderStatisticsVO;
import org.bigbrother.vo.OrderSubmitVO;
import org.bigbrother.vo.OrderVO;

public interface OrderService {

    /**
     * 用户下单
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 用户端订单分页查询
     */
    PageResult pageQuery4User(int page, int pageSize, Integer status);

    /**
     * 查询订单详情
     */
    OrderVO details(Long id);

    /**
     * 取消订单
     */
    void cancel(Long id);

    /**
     * 再来一单
     */
    void repetition(Long id);

    /**
     * 支付成功，修改订单状态
     */
    void paySuccess(String outTradeNo);

    /**
     * 订单搜索
     * @param ordersPageQueryDTO 搜索条件
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 各个状态的订单数量统计
     */
    OrderStatisticsVO statistics();

    /**
     * 接单
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 拒单
     */
    void rejection(OrdersRejectionDTO ordersRejectionDTO);

    /**
     * 商家取消订单
     */
    void cancel(OrdersCancelDTO ordersCancelDTO);

    /**
     * 派送订单
     */
    void delivery(Long id);

    /**
     * 完成订单
     */
    void complete(Long id);
}
