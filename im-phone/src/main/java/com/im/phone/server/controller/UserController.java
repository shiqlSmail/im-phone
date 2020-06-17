package com.im.phone.server.controller;

import com.im.phone.server.request.CheckPhoneRequest;
import com.im.phone.server.request.IMLoginRequest;
import com.im.phone.server.request.UserRegisterRequest;
import com.im.phone.server.util.IpUtils;
import com.im.phone.server.xml.MessageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
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
    public String toLogin(@RequestBody IMLoginRequest param){
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

    /**
     * 用户注册
     *
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "register")
    @ResponseBody
    public String  toRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, Object> map = sendXmlMsg("1001001");
        Map<String,Object> bodyMap = new HashMap<>();
        bodyMap.put("ip", IpUtils.getIpAddr(request));
        bodyMap.put("password",userRegisterRequest.getPassword());
        bodyMap.put("phone",userRegisterRequest.getPhone());
        bodyMap.put("smscode",userRegisterRequest.getSmscode());

        String xml = MessageUtils.mapToXml(map,bodyMap);
        log.info("请求的xml信息为："+xml);
        String response = toSendPostXml("1001", xml);
        log.info("最终返回的结果为："+response);
        return response;
    }


    /**
     * 检查手机号是否存在
     *
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "checkPhone")
    @ResponseBody
    public String  checkPhone(@RequestBody CheckPhoneRequest checkPhoneRequest){
        Map<String, Object> map = sendXmlMsg("1001003");
        Map<String,Object> bodyMap = new HashMap<>();
        bodyMap.put("phone",checkPhoneRequest.getPhone());

        String xml = MessageUtils.mapToXml(map,bodyMap);
        log.info("请求的xml信息为："+xml);
        String response = toSendPostXml("1001", xml);
        log.info("最终返回的结果为："+response);
        return response;
    }
}