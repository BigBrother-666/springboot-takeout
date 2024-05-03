package org.bigbrother.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.bigbrother.constant.RedisKeyConstant;
import org.bigbrother.constant.StatusConstant;
import org.bigbrother.entity.Dish;
import org.bigbrother.result.Result;
import org.bigbrother.service.DishService;
import org.bigbrother.vo.DishVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("UserDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "菜品浏览接口")
public class DishController {
    private final DishService dishService;
    private final RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    public DishController(DishService dishService,
                          RedisTemplate<Object, Object> redisTemplate) {
        this.dishService = dishService;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {
        // 尝试从redis中获取数据
        String key = RedisKeyConstant.KEY_CATEGORY_PREFIX + categoryId;
        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);
        if (list != null && !list.isEmpty()) {
            // 数据在redis，直接返回
            return Result.success(list);
        }
        // 数据不在redis，查询数据库
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE); //查询起售中的菜品
        list = dishService.listWithFlavor(dish);
        // 放入redis
        redisTemplate.opsForValue().set(key, list);
        return Result.success(list);
    }

}
