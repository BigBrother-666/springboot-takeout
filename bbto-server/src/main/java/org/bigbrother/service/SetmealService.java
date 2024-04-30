package org.bigbrother.service;


import org.bigbrother.dto.DishDTO;
import org.bigbrother.dto.SetmealDTO;
import org.bigbrother.dto.SetmealPageQueryDTO;
import org.bigbrother.result.PageResult;
import org.bigbrother.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO 查询参数
     * @return 分页结果
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 新增套餐及套餐菜品
     * @param setmealDTO 新增的套餐信息
     */
    void saveWithDish(SetmealDTO setmealDTO);

    /**
     * 批量删除套餐
     * @param ids 删除的id
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据套餐id查询套餐信息
     * @param id 待查询的套餐id
     * @return 套餐信息
     */
    SetmealVO getById(Long id);

    /**
     * 修改套餐信息
     * @param setmealDTO 待修改的数据
     */
    void updateWithDish(SetmealDTO setmealDTO);

    /**
     * 启用/禁用套餐
     * @param status 修改后的状态
     * @param id 修改的套餐id
     */
    void changeDishStatus(Integer status, Long id);
}
