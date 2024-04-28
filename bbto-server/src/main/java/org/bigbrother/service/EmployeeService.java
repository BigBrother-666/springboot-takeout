package org.bigbrother.service;

import org.bigbrother.dto.EmployeeDTO;
import org.bigbrother.dto.EmployeeLoginDTO;
import org.bigbrother.entity.Employee;

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
}
