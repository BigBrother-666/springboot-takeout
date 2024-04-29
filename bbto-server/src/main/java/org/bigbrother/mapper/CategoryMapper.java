package org.bigbrother.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.bigbrother.annotation.AutoFill;
import org.bigbrother.dto.CategoryPageQueryDTO;
import org.bigbrother.entity.Category;
import org.bigbrother.enumeration.OperationType;

import java.util.List;

@Mapper
public interface CategoryMapper {

    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    @AutoFill(value = OperationType.INSERT)
    @Insert("insert into category(id, type, name, sort, status, create_time, update_time, create_user, update_user) " +
            "values(#{id},#{type},#{name},#{sort},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void save(Category category);

    @AutoFill(value = OperationType.UPDATE)
    void update(Category category);

    @Select("select * from category where type=#{type}")
    List<Category> getByType(String type);

    @Delete("delete from category where id = #{id}")
    void deleteById(Long id);

    List<Category> list(Integer type);
}
