package org.bigbrother.service;

import org.bigbrother.dto.UserLoginDTO;
import org.bigbrother.entity.User;

public interface UserService {
    /**
     * 微信登录
     * @param userLoginDTO 登录code
     * @return 用户信息
     */
    User vxLogin(UserLoginDTO userLoginDTO);
}
