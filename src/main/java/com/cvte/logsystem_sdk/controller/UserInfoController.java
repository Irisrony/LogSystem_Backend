package com.cvte.logsystem_sdk.controller;

import com.cvte.logsystem_sdk.domain.UserInfo;
import com.cvte.logsystem_sdk.service.UserInfoService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserInfoController {
    @Resource
    private UserInfoService userInfoService;

    /**
     * 检查用户是否需要上传
     * @param userInfo  用户信息
     * @return  是否上传
     */
    @PostMapping("/isExist")
    public Map<String,Boolean> isExist(@Valid @RequestBody UserInfo userInfo){
        Map<String,Boolean> res = new HashMap<>();
        Boolean status = userInfoService.isExist(userInfo);
        res.put("status",status);
        return res;
    }
}
