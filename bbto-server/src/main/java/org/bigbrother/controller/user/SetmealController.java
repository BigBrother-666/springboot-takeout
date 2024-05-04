package org.bigbrother.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.bigbrother.constant.RedisKeyConstant;
import org.bigbrother.constant.StatusConstant;
import org.bigbrother.dto.SetmealDTO;
import org.bigbrother.dto.SetmealPageQueryDTO;
import org.bigbrother.entity.Setmeal;
import org.bigbrother.result.PageResult;
import org.bigbrother.result.Result;
import org.bigbrother.service.SetmealService;
import org.bigbrother.vo.DishItemVO;
import org.bigbrother.vo.SetmealVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("UserSetmealController")
@RequestMapping("/user/setmeal")
@Api(tags = "套餐相关接口")
@Slf4j
public class SetmealController {


    private final SetmealService setmealService;

    @Autowired
    public SetmealController(SetmealService setmealService) {
        this.setmealService = setmealService;
    }


    @GetMapping("/list")
    @Cacheable(cacheNames = RedisKeyConstant.KEY_SETMEAL_PREFIX, key = "#categoryId")
    @ApiOperation("根据分类id查询套餐")
    public Result<List<Setmeal>> list(Long categoryId) {
        Setmeal setmeal = new Setmeal();
        setmeal.setCategoryId(categoryId);
        setmeal.setStatus(StatusConstant.ENABLE);
        List<Setmeal> list = setmealService.list(setmeal);
        return Result.success(list);
    }

    @GetMapping("/dish/{id}")
    @ApiOperation("根据套餐id查询包含的菜品列表")
    public Result<List<DishItemVO>> dishList(@PathVariable("id") Long id) {
        List<DishItemVO> list = setmealService.getDishItemById(id);
        return Result.success(list);
    }
}
