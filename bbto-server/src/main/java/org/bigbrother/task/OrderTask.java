package org.bigbrother.task;


import lombok.extern.slf4j.Slf4j;
import org.bigbrother.constant.MessageConstant;
import org.bigbrother.entity.Orders;
import org.bigbrother.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时处理订单状态
 */
@Component
@Slf4j
public class OrderTask {
    private final OrderMapper orderMapper;

    @Autowired
    public OrderTask(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    /**
     * 检测15分钟未付款的订单
     * 每分钟触发一次
     */
    @Scheduled(cron = "0 * * * * ? ")
    public void processTimeoutOrder() {
        log.info("定时处理超时订单");
        // 查询15分钟前且待付款的订单
        List<Orders> orders = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, LocalDateTime.now().plusMinutes(-15));
        if (orders != null && !orders.isEmpty()) {
            for (Orders order : orders) {
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason(MessageConstant.ORDER_AUTO_CANCELED);
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
            }
        }
    }

    /**
     * 处理派送中订单
     * 每天凌晨1点触发
     */
    @Scheduled(cron = "0 0 1 * * ? ")
    public void processDeliveryOrder(){
        // 查询前一天派送中的订单
        log.info("定时处理派送中订单");
        List<Orders> orders = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now().plusHours(-1));
        if (orders != null && !orders.isEmpty()) {
            for (Orders order : orders) {
                order.setStatus(Orders.CONFIRMED);
                orderMapper.update(order);
            }
        }
    }
}
