package org.bigbrother.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.bigbrother.entity.User;

@Mapper
public interface UserMapper {
    /**
     * 根据openid查询用户
     * @param openid openid
     * @return user对象
     */
    @Select("select * from user where openid=#{openid}")
    User getByOpenid(String openid);

    void insert(User user);
}
