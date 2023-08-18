package com.cvte.logsystem.controller;

import com.cvte.logsystem.aop.annotation.LogRecord;
import com.cvte.logsystem.exception.LoginException;
import com.cvte.logsystem.service.UserService;
import com.cvte.logsystem.domain.User;
import com.cvte.logsystem.response.ResultCode;
import com.cvte.logsystem.utils.CookieUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    private final static String KEY = "token";
    private final static int EXPIRE_TIME = 24*60*60;

    /**
     * 管理员登陆
     * @param user  用户登陆实体
     * @param response  响应
     * @return  返回处理结果
     */
    @PostMapping("/login")
    @LogRecord
    public Map<?,?> login(@Valid @RequestBody User user, HttpServletResponse response) {
        String token = userService.login(user);
        if (token == null) {
            throw new LoginException(ResultCode.USER_LOGIN_ERROR);
        }

        CookieUtils.set(response,KEY,token,EXPIRE_TIME,true);

        return new HashMap<>();
    }

    /**
     * 管理员登出
     * @param response  响应
     * @return  返回处理结果
     */
    @PostMapping("/logout")
    public Map<?,?> logout(HttpServletResponse response) {
        CookieUtils.set(response,KEY,null,0,true);
        return new HashMap<>();
    }

}
