package org.bigbrother.service.impl;

import org.bigbrother.constant.RedisKeyConstant;
import org.bigbrother.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ShopServiceImpl implements ShopService {
    private final RedisTemplate redisTemplate;

    @Autowired
    public ShopServiceImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void changeStatus(Integer status) {
        redisTemplate.opsForValue().set(RedisKeyConstant.KEY_SHOP_STATUS, status);
    }

    @Override
    public Integer getStatus() {
        return (Integer) redisTemplate.opsForValue().get(RedisKeyConstant.KEY_SHOP_STATUS);
    }
}
