package org.bigbrother.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.bigbrother.dto.DishDTO;
import org.bigbrother.dto.DishPageQueryDTO;
import org.bigbrother.entity.Dish;
import org.bigbrother.result.PageResult;
import org.bigbrother.result.Result;
import org.bigbrother.service.DishService;
import org.bigbrother.vo.DishVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {

    private final DishService dishService;

    @Autowired
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @PostMapping
    @ApiOperation("新增菜品")
    public Result<String> save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品：{}", dishDTO);
        dishService.saveWithFlavor(dishDTO);
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
        return Result.success();
    }

    @ApiOperation("启用/禁用菜品")
    @PostMapping("/status/{status}")
    public Result<String> changeUserStatus(@PathVariable Integer status, Long id) {
        log.info("修改id={}菜品状态为{}", id, status);
        dishService.changeDishStatus(status, id);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(Long categoryId){
        log.info("查询分类id={}包含的菜品", categoryId);
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }
}
