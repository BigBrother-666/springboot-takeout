package org.bigbrother.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.bigbrother.constant.MessageConstant;
import org.bigbrother.dto.UserLoginDTO;
import org.bigbrother.entity.User;
import org.bigbrother.exception.LoginFailedException;
import org.bigbrother.mapper.UserMapper;
import org.bigbrother.properties.WeChatProperties;
import org.bigbrother.service.UserService;
import org.bigbrother.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    private static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";
    private final WeChatProperties weChatProperties;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(WeChatProperties weChatProperties, UserMapper userMapper) {
        this.weChatProperties = weChatProperties;
        this.userMapper = userMapper;
    }

    @Override
    public User vxLogin(UserLoginDTO userLoginDTO) {
        String openid = getOpenid(userLoginDTO.getCode());
        // openid为空 登录失败
        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        // 判断当前用户是否为新用户 新用户则注册
        User user = userMapper.getByOpenid(openid);

        if (user == null) {
            // 是新用户
            user = User.builder().openid(openid).createTime(LocalDateTime.now()).build();
            userMapper.insert(user);
        }
        return user;
    }

    private String getOpenid(String code) {
        // 调用微信接口获取openid
        Map<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, map);
        JSONObject jsonObject = JSON.parseObject(json);
        return jsonObject.getString("openid");
    }
}
