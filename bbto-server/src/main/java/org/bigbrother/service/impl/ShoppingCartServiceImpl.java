package org.bigbrother.service.impl;

import org.bigbrother.context.BaseContext;
import org.bigbrother.dto.ShoppingCartDTO;
import org.bigbrother.entity.Dish;
import org.bigbrother.entity.Setmeal;
import org.bigbrother.entity.ShoppingCartItem;
import org.bigbrother.mapper.DishMapper;
import org.bigbrother.mapper.SetmealMapper;
import org.bigbrother.mapper.ShoppingCartMapper;
import org.bigbrother.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartMapper shoppingCartMapper;
    private final DishMapper dishMapper;
    private final SetmealMapper setmealMapper;

    @Autowired
    public ShoppingCartServiceImpl(ShoppingCartMapper shoppingCartMapper,
                                   DishMapper dishMapper,
                                   SetmealMapper setmealMapper) {
        this.shoppingCartMapper = shoppingCartMapper;
        this.dishMapper = dishMapper;
        this.setmealMapper = setmealMapper;
    }

    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCartItem);
        shoppingCartItem.setUserId(BaseContext.getCurrentId());
        // 查询添加的菜品是否已经在购物车
        List<ShoppingCartItem> list = shoppingCartMapper.list(shoppingCartItem);

        if (list != null && !list.isEmpty()) {
            // 该菜品在购物车已存在，则只需数量+1
            ShoppingCartItem item = list.get(0);
            item.setNumber(item.getNumber() + 1);
            shoppingCartMapper.updateNumberById(item);
        } else {
            // 否则将该菜品添加至购物车
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null) {
                // 添加购物车的是菜品
                Dish dish = dishMapper.getById(dishId);
                shoppingCartItem.setName(dish.getName());
                shoppingCartItem.setImage(dish.getImage());
                shoppingCartItem.setAmount(dish.getPrice());
            } else {
                // 添加购物车的是套餐
                Long setmealId = shoppingCartDTO.getSetmealId();
                Setmeal setmeal = setmealMapper.getById(setmealId);
                shoppingCartItem.setName(setmeal.getName());
                shoppingCartItem.setImage(setmeal.getImage());
                shoppingCartItem.setAmount(setmeal.getPrice());
            }
            shoppingCartItem.setNumber(1);
            shoppingCartItem.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCartItem);
        }

    }

    @Override
    public List<ShoppingCartItem> list() {
        return shoppingCartMapper.list(ShoppingCartItem.builder()
                .userId(BaseContext.getCurrentId())
                .build());
    }

    @Override
    public void clean() {
        shoppingCartMapper.clean(BaseContext.getCurrentId());
    }
}
