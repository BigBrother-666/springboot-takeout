package org.bigbrother.service;

import org.bigbrother.dto.CategoryDTO;
import org.bigbrother.dto.CategoryPageQueryDTO;
import org.bigbrother.entity.Category;
import org.bigbrother.entity.Employee;
import org.bigbrother.result.PageResult;

import java.util.List;

public interface CategoryService {
    /**
     * 分类分页查询
     * @param categoryPageQueryDTO 分页信息
     * @return 分页结果
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 新增分类
     * @param categoryDTO 分类信息
     */
    void save(CategoryDTO categoryDTO);

    /**
     * 修改分类启用/禁用
     * @param status 修改后的状态
     * @param id 分类id
     */
    void changeCategoryStatus(Integer status, Long id);

    /**
     * 根据type查询分类
     * @param type 待查询的type
     * @return 查询结果
     */
    List<Category> getByType(String type);

    /**
     * 修改分类信息
     * @param categoryDTO 待修改的信息
     */
    void update(CategoryDTO categoryDTO);

    /**
     * @param id 待删除的id
     */
    void deleteById(Long id);
}
