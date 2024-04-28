package org.bigbrother.mapper;

import org.apache.ibatis.annotations.Insert;
import org.bigbrother.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username 员工名
     * @return 员工信息
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);


    /**
     * 插入员工信息
     * @param employee 员工信息
     */
    @Insert("insert into employee (id, name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user) " +
            "values (#{id},#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void insert(Employee employee);
}