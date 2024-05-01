package org.bigbrother.service;

public interface ShopService {
    /**
     * 改变店铺状态
     * @param status 设置的状态
     */
    void changeStatus(Integer status);

    /**
     * 获取店铺状态
     * @return 店铺状态
     */
    Integer getStatus();
}
