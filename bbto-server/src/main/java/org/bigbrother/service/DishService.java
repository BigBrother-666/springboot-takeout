package org.bigbrother.service;

import org.bigbrother.dto.DishDTO;
import org.bigbrother.dto.DishPageQueryDTO;
import org.bigbrother.entity.Dish;
import org.bigbrother.result.PageResult;
import org.bigbrother.vo.DishVO;

import java.util.List;

public interface DishService {
    /**
     * 保存菜品（包括口味）
     * @param dishDTO 菜品信息
     */
    void saveWithFlavor(DishDTO dishDTO);

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO 分页查询参数
     * @return 查询结果
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 批量删除菜品
     * @param ids 菜品的id
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据id查询菜品信息
     * @param id 菜品id
     */
    DishVO getById(Long id);

    /**
     * 修改菜品和口味
     * @param dishDTO 待修改的数据
     */
    void updateWithFlavor(DishDTO dishDTO);

    /**
     * 启用/禁用菜品
     * @param status 修改后的状态
     * @param id 菜品id
     */
    void changeDishStatus(Integer status, Long id);

    /**
     * 根据分类id查询菜品
     * @param categoryId 分类id
     * @return 查询结果
     */
    List<Dish> list(Long categoryId);

    /**
     * 条件查询菜品和口味
     */
    List<DishVO> listWithFlavor(Dish dish);
}
