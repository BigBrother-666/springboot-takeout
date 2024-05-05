package org.bigbrother.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.bigbrother.entity.ShoppingCartItem;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    List<ShoppingCartItem> list(ShoppingCartItem shoppingCartItem);

    @Update("update shopping_cart set number=#{number} where id=#{id}")
    void updateNumberById(ShoppingCartItem item);

    @Insert("insert into shopping_cart(name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time) " +
            "values (#{name},#{image},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount},#{createTime})")
    void insert(ShoppingCartItem shoppingCartItem);

    @Delete("delete from shopping_cart where user_id=#{userid}")
    void clean(Long userid);

    @Delete("delete from shopping_cart where id=#{id}")
    void deleteById(Long id);

    @Delete("delete from shopping_cart where user_id=#{userId}")
    void deleteByUserId(Long userId);

    void insertBatch(List<ShoppingCartItem> shoppingCartList);
}
