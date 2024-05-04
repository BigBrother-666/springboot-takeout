package org.bigbrother.service;

import org.bigbrother.dto.ShoppingCartDTO;
import org.bigbrother.entity.ShoppingCartItem;

import java.util.List;

public interface ShoppingCartService {

    /**
     * 添加购物车
     * @param shoppingCartDTO 添加的商品
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查询购物车
     * @return 购物车数据
     */
    List<ShoppingCartItem> list();

    /**
     * 清空购物车
     */
    void clean();
}
