package org.bigbrother.service;

import org.bigbrother.dto.EmployeeDTO;
import org.bigbrother.dto.EmployeeLoginDTO;
import org.bigbrother.dto.EmployeePageQueryDTO;
import org.bigbrother.entity.Employee;
import org.bigbrother.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO 登录信息
     * @return 员工信息
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     * @param employeeDTO 员工信息
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 分页查询
     * @param employeePageQueryDTO 分页查询参数
     * @return 信息
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用/禁用员工账号
     * @param status 修改到的状态
     * @param id 修改的员工id
     */
    void changeUserStatus(Integer status, Long id);

    /**
     * 根据id查询员工信息
     * @param id 员工id
     * @return 员工信息
     */
    Employee getById(Long id);

    /**
     * 修改员工信息
     * @param employeeDTO 员工现有信息
     */
    void update(EmployeeDTO employeeDTO);
}
