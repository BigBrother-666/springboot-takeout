package org.bigbrother.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.bigbrother.dto.CategoryDTO;
import org.bigbrother.dto.CategoryPageQueryDTO;
import org.bigbrother.entity.Category;
import org.bigbrother.result.PageResult;
import org.bigbrother.result.Result;
import org.bigbrother.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(value = "分类相关接口")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/page")
    @ApiOperation("分类分页查询")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("分类分页查询参数：{}", categoryPageQueryDTO);
        return Result.success(categoryService.pageQuery(categoryPageQueryDTO));
    }

    @ApiOperation("新增分类")
    @PostMapping
    public Result<String> save(@RequestBody CategoryDTO categoryDTO) {
        log.info("新增分类：{}", categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }

    @ApiOperation("启用/禁用分类")
    @PostMapping("/status/{status}")
    public Result<String> changeCategoryStatus(@PathVariable Integer status, Long id) {
        log.info("修改id={}分类状态为{}", id, status);
        categoryService.changeCategoryStatus(status, id);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("根据类型查询分类")
    public Result<List<Category>> getById(String type) {
        log.info("查询type={}分类", type);
        List<Category> categories = categoryService.getByType(type);
        return Result.success(categories);
    }

    @ApiOperation("修改分类")
    @PutMapping
    public Result<String> update(@RequestBody CategoryDTO categoryDTO) {
        log.info("修改id={}分类", categoryDTO.getId());
        categoryService.update(categoryDTO);
        return Result.success();
    }

    @ApiOperation("根据id删除分类")
    @DeleteMapping
    public Result<String> delete(Long id) {
        log.info("删除id={}分类", id);
        categoryService.deleteById(id);
        return Result.success();
    }
}
