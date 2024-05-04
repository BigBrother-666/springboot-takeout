package org.bigbrother.service.impl;

import org.bigbrother.constant.MessageConstant;
import org.bigbrother.context.BaseContext;
import org.bigbrother.dto.OrdersSubmitDTO;
import org.bigbrother.entity.AddressBook;
import org.bigbrother.entity.OrderDetail;
import org.bigbrother.entity.Orders;
import org.bigbrother.entity.ShoppingCartItem;
import org.bigbrother.exception.AddressBookBusinessException;
import org.bigbrother.exception.ShoppingCartBusinessException;
import org.bigbrother.mapper.AddressBookMapper;
import org.bigbrother.mapper.OrderDetailMapper;
import org.bigbrother.mapper.OrderMapper;
import org.bigbrother.mapper.ShoppingCartMapper;
import org.bigbrother.service.OrderService;
import org.bigbrother.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderDetailMapper orderDetailMapper;
    private final AddressBookMapper addressBookMapper;
    private final ShoppingCartMapper shoppingCartMapper;

    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper,
                            OrderDetailMapper orderDetailMapper,
                            AddressBookMapper addressBookMapper,
                            ShoppingCartMapper shoppingCartMapper) {
        this.orderMapper = orderMapper;
        this.orderDetailMapper = orderDetailMapper;
        this.addressBookMapper = addressBookMapper;
        this.shoppingCartMapper = shoppingCartMapper;
    }

    @Override
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        // 校验地址和购物车
        AddressBook address = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (address == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        Long currentId = BaseContext.getCurrentId();
        ShoppingCartItem item = ShoppingCartItem.builder().userId(currentId).build();
        List<ShoppingCartItem> list = shoppingCartMapper.list(item);
        if (list == null || list.isEmpty()) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }
        // 构造Orders并向订单表插入一条数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);  // 未付款
        orders.setStatus(Orders.PENDING_PAYMENT);  // 待付款
        orders.setNumber(String.valueOf(System.currentTimeMillis()) + currentId);
        orders.setPhone(address.getPhone());
        orders.setConsignee(address.getConsignee());
        orders.setUserId(currentId);
        orderMapper.insert(orders);
        // 向订单表插入多条数据
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCartItem cartItem : list) {
            OrderDetail orderDetail = new OrderDetail();  // 订单明细(菜品信息)
            BeanUtils.copyProperties(cartItem, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetailList.add(orderDetail);
        }
        orderDetailMapper.insertBatch(orderDetailList);  // 批量插入
        // 清空用户购物车
        shoppingCartMapper.deleteByUserId(currentId);
        // 封装vo返回结果
        return OrderSubmitVO.builder()
                .id(orders.getId())
                .orderTime(orders.getOrderTime())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .build();
    }
}
