package org.bigbrother.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.bigbrother.constant.JwtClaimsConstant;
import org.bigbrother.dto.EmployeeDTO;
import org.bigbrother.dto.EmployeeLoginDTO;
import org.bigbrother.dto.EmployeePageQueryDTO;
import org.bigbrother.entity.Employee;
import org.bigbrother.properties.JwtProperties;
import org.bigbrother.result.PageResult;
import org.bigbrother.result.Result;
import org.bigbrother.service.EmployeeService;
import org.bigbrother.utils.JwtUtil;
import org.bigbrother.vo.EmployeeLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(value = "员工相关接口")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final JwtProperties jwtProperties;

    @Autowired
    public EmployeeController(EmployeeService employeeService, JwtProperties jwtProperties) {
        this.employeeService = employeeService;
        this.jwtProperties = jwtProperties;
    }

    /**
     * 登录
     *
     * @param employeeLoginDTO 登录用户实体类
     * @return 处理结果
     */
    @ApiOperation("员工登录")
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return 处理结果
     */

    @ApiOperation("员工登出")
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    @ApiOperation("新增员工")
    @PostMapping
    public Result<String> save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("新增员工：{}", employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("员工分页查询")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页查询参数：{}", employeePageQueryDTO);
        return Result.success(employeeService.pageQuery(employeePageQueryDTO));
    }

    @ApiOperation("启用/禁用员工账号")
    @PostMapping("/status/{status}")
    public Result<String> changeUserStatus(@PathVariable Integer status, Long id) {
        log.info("修改id={}员工状态为{}", id, status);
        employeeService.changeUserStatus(status, id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询员工信息")
    public Result<Employee> getById(@PathVariable Long id) {
        log.info("查询id={}员工", id);
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    @ApiOperation("修改员工信息")
    @PutMapping
    public Result<String> update(@RequestBody EmployeeDTO employeeDTO) {
        log.info("修改id={}员工信息", employeeDTO.getId());
        employeeService.update(employeeDTO);
        return Result.success();
    }
}
