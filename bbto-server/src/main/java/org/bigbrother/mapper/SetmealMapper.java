package org.bigbrother.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.bigbrother.annotation.AutoFill;
import org.bigbrother.dto.SetmealPageQueryDTO;
import org.bigbrother.entity.Setmeal;
import org.bigbrother.enumeration.OperationType;
import org.bigbrother.vo.SetmealVO;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 根据分类id更新套餐
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * 根据id查询套餐
     */
    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    /**
     * 根据id删除套餐
     */
    @Delete("delete from setmeal where id = #{id}")
    void deleteById(Long setmealId);
}
