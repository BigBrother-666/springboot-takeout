package org.bigbrother.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.bigbrother.dto.SetmealDTO;
import org.bigbrother.dto.SetmealPageQueryDTO;
import org.bigbrother.result.PageResult;
import org.bigbrother.result.Result;
import org.bigbrother.service.SetmealService;
import org.bigbrother.vo.SetmealVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
@Slf4j
public class SetmealController {


    private final SetmealService setmealService;

    @Autowired
    public SetmealController(SetmealService setmealService) {
        this.setmealService = setmealService;
    }

    @PostMapping
    @ApiOperation("新增套餐")
    public Result<String> save(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐：{}", setmealDTO);
        setmealService.saveWithDish(setmealDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("套餐分页查询")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("套餐分页查询参数：{}", setmealPageQueryDTO);
        return Result.success(setmealService.pageQuery(setmealPageQueryDTO));
    }

    @ApiOperation("批量删除套餐")
    @DeleteMapping
    public Result<String> delete(@RequestParam List<Long> ids) {
        log.info("删除id={}套餐", ids);
        setmealService.deleteByIds(ids);
        return Result.success();
    }

    @ApiOperation("根据id查询套餐")
    @GetMapping("/{id}")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        log.info("查询id={}套餐", id);
        SetmealVO setmealVO = setmealService.getById(id);
        return Result.success(setmealVO);
    }

    @PutMapping
    @ApiOperation("修改套餐")
    public Result<String> update(@RequestBody SetmealDTO setmealDTO) {
        log.info("修改id={}套餐", setmealDTO.getId());
        setmealService.updateWithDish(setmealDTO);
        return Result.success();
    }

    @ApiOperation("启用/禁用套餐")
    @PostMapping("/status/{status}")
    public Result<String> changeUserStatus(@PathVariable Integer status, Long id) {
        log.info("修改id={}套餐状态为{}", id, status);
        setmealService.changeDishStatus(status, id);
        return Result.success();
    }
}
