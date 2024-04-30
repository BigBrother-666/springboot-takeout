package org.bigbrother.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.bigbrother.constant.MessageConstant;
import org.bigbrother.constant.StatusConstant;
import org.bigbrother.dto.SetmealDTO;
import org.bigbrother.dto.SetmealPageQueryDTO;
import org.bigbrother.entity.Dish;
import org.bigbrother.entity.Setmeal;
import org.bigbrother.entity.SetmealDish;
import org.bigbrother.exception.DeletionNotAllowedException;
import org.bigbrother.exception.SetmealEnableFailedException;
import org.bigbrother.mapper.DishMapper;
import org.bigbrother.mapper.SetmealDishMapper;
import org.bigbrother.mapper.SetmealMapper;
import org.bigbrother.result.PageResult;
import org.bigbrother.service.SetmealService;
import org.bigbrother.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class SetmealServiceImpl implements SetmealService {
    private final SetmealMapper setmealMapper;
    private final SetmealDishMapper setmealDishMapper;
    private final DishMapper dishMapper;

    @Autowired
    public SetmealServiceImpl(SetmealMapper setmealMapper,
                              SetmealDishMapper setmealDishMapper,
                              DishMapper dishMapper) {
        this.setmealMapper = setmealMapper;
        this.setmealDishMapper = setmealDishMapper;
        this.dishMapper = dishMapper;
    }

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        //向套餐表插入数据
        setmealMapper.insert(setmeal);

        //获取生成的套餐id
        Long setmealId = setmeal.getId();
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });

        //保存套餐和菜品的关联关系
        setmealDishMapper.insertBatch(setmealDishes);
    }

    @Transactional
    @Override
    public void deleteByIds(List<Long> ids) {
        ids.forEach(id -> {
            Setmeal setmeal = setmealMapper.getById(id);
            if (StatusConstant.ENABLE.equals(setmeal.getStatus())) {
                //起售中的套餐不能删除
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });

        ids.forEach(setmealId -> {
            //删除套餐表中的数据
            setmealMapper.deleteById(setmealId);
            //删除套餐菜品关系表中的数据
            setmealDishMapper.deleteBySetmealId(setmealId);
        });
    }

    @Override
    public SetmealVO getById(Long id) {
        // 查询套餐基础信息
        Setmeal setmeal = setmealMapper.getById(id);
        // 查询套餐菜品信息
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);
        // 封装数据
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    @Transactional
    @Override
    public void updateWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        //修改套餐表，执行update
        setmealMapper.update(setmeal);

        //删除套餐和菜品的关联关系，操作setmeal_dish表，执行delete
        Long setmealId = setmealDTO.getId();
        setmealDishMapper.deleteBySetmealId(setmealId);

        //重新插入套餐和菜品的关联关系，操作setmeal_dish表，执行insert
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));
        setmealDishMapper.insertBatch(setmealDishes);
    }

    @Override
    public void changeDishStatus(Integer status, Long id) {
        //起售套餐时，判断套餐内是否有停售菜品，有停售菜品提示"套餐内包含未启售菜品，无法启售"
        if(Objects.equals(status, StatusConstant.ENABLE)){
            List<Dish> dishList = dishMapper.getBySetmealId(id);
            if(dishList != null && !dishList.isEmpty()){
                dishList.forEach(dish -> {
                    if(StatusConstant.DISABLE.equals(dish.getStatus())){
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }

        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);
    }
}
