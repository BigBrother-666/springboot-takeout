package org.bigbrother.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.bigbrother.constant.RedisKeyConstant;
import org.bigbrother.dto.DishDTO;
import org.bigbrother.dto.DishPageQueryDTO;
import org.bigbrother.entity.Dish;
import org.bigbrother.result.PageResult;
import org.bigbrother.result.Result;
import org.bigbrother.service.DishService;
import org.bigbrother.vo.DishVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController("AdminDishController")
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {

    private final DishService dishService;
    private final RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    public DishController(DishService dishService,
                          RedisTemplate<Object, Object> redisTemplate) {
        this.dishService = dishService;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping
    @ApiOperation("新增菜品")
    public Result<String> save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品：{}", dishDTO);
        dishService.saveWithFlavor(dishDTO);
        cleanRedisCache(RedisKeyConstant.KEY_CATEGORY_PREFIX + dishDTO.getCategoryId());
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询参数：{}", dishPageQueryDTO);
        return Result.success(dishService.pageQuery(dishPageQueryDTO));
    }

    @ApiOperation("批量删除菜品")
    @DeleteMapping
    public Result<String> delete(@RequestParam List<Long> ids) {
        log.info("删除id={}菜品", ids);
        dishService.deleteByIds(ids);
        cleanRedisCache(RedisKeyConstant.KEY_CATEGORY_PREFIX + "*");
        return Result.success();
    }

    @ApiOperation("根据id查询菜品")
    @GetMapping("/{id}")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("查询id={}菜品", id);
        DishVO dishVO = dishService.getById(id);
        return Result.success(dishVO);
    }

    @PutMapping
    @ApiOperation("修改菜品")
    public Result<String> update(@RequestBody DishDTO dishDTO) {
        log.info("修改id={}菜品", dishDTO.getId());
        dishService.updateWithFlavor(dishDTO);
        cleanRedisCache(RedisKeyConstant.KEY_CATEGORY_PREFIX + "*");
        return Result.success();
    }

    @ApiOperation("启用/禁用菜品")
    @PostMapping("/status/{status}")
    public Result<String> changeUserStatus(@PathVariable Integer status, Long id) {
        log.info("修改id={}菜品状态为{}", id, status);
        dishService.changeDishStatus(status, id);
        cleanRedisCache(RedisKeyConstant.KEY_CATEGORY_PREFIX + "*");
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(Long categoryId) {
        log.info("查询分类id={}包含的菜品", categoryId);
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }

    /**
     * 清理redis指定key缓存
     * @param pattern 匹配的key
     */
    private void cleanRedisCache(String pattern) {
        Set<Object> keys = redisTemplate.keys(pattern);
        if (keys != null) {
            redisTemplate.delete(keys);
        }
    }
}
