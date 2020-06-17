package com.im.phone.server.controller;

import com.im.phone.server.request.IlonwLoginRequest;
import com.im.phone.server.xml.MessageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户中心
 *
 * @author shiqilong
 */
@RestController
@RequestMapping("/user")
@Api(description = "用户接口")
public class UserController extends BaseControler {

    /**
     * 登录接口
     *
     * @param param
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @PostMapping("/login")
    @ApiOperation(value="用户登录", notes="用户接口")
    public String toLogin(@RequestBody IlonwLoginRequest param){
        Map<String, Object> map = sendXmlMsg("1001002");
        Map<String,Object> bodyMap = new HashMap<>();
        bodyMap.put("phone", param.getPhone());
        bodyMap.put("password", param.getPassword());
        bodyMap.put("loginType", param.getLoginType());
        bodyMap.put("smscode", param.getSmsCode());
        String xml = MessageUtils.mapToXml(map,bodyMap);
        log.info("请求的xml信息为："+xml);
        String response = toSendPostXml("1001", xml);
        log.info("最终返回的结果为："+response);
        return response;
    }
}