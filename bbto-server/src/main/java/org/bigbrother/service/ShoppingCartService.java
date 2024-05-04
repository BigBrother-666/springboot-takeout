package org.bigbrother.service;

import org.bigbrother.dto.ShoppingCartDTO;

public interface ShoppingCartService {

    /**
     * 添加购物车
     * @param shoppingCartDTO 添加的商品
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
