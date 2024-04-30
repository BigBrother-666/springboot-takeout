package org.bigbrother.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.bigbrother.annotation.AutoFill;
import org.bigbrother.dto.DishPageQueryDTO;
import org.bigbrother.entity.Dish;
import org.bigbrother.enumeration.OperationType;
import org.bigbrother.vo.DishVO;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    @Select("select * from dish where id=#{id}")
    Dish getById(Long id);

    @Delete("delete from dish where id=#{id}")
    void deleteById(Long id);

    void deleteByIds(List<Long> ids);

    /**
     * 根据id修改菜品信息
     * @param dish 待修改的信息
     */
    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    /**
     * 动态条件查询菜品
     * @param dish 条件
     * @return 查询结果
     */
    List<Dish> list(Dish dish);

    @Select("select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = #{setmealId}")
    List<Dish> getBySetmealId(Long setmealId);
}

