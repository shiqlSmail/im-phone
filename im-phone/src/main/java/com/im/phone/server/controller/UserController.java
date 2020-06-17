package com.im.phone.server.controller;

import com.im.phone.server.common.Constants;
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
 * 方法请求参数说明：
 *      responseResult(1,2,3);
 *      1：代表每个交易的交易码，统一配置在Constants类
 *      2：代表请求需要的参数，可从ESB控制台找到
 *      3：代表ESB连接路径，写死，具体改变配置在配置文件中
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
        Map<String,Object> bodyMap = new HashMap<>();
        bodyMap.put("phone", param.getPhone());
        bodyMap.put("password", param.getPassword());
        bodyMap.put("loginType", param.getLoginType());
        bodyMap.put("smscode", param.getSmsCode());
        return responseResult(Constants.USER_LOGIN,bodyMap,Constants.ESB);
    }

    /**
     * 用户注册
     *
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @PostMapping(value = "register")
    @ResponseBody
    public String  toRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String,Object> bodyMap = new HashMap<>();
        bodyMap.put("ip", IpUtils.getIpAddr(request));
        bodyMap.put("password",userRegisterRequest.getPassword());
        bodyMap.put("phone",userRegisterRequest.getPhone());
        bodyMap.put("smscode",userRegisterRequest.getSmscode());
        return responseResult(Constants.USER_REGISTER,bodyMap,Constants.ESB);
    }


    /**
     * 检查手机号是否存在
     *
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @PostMapping(value = "checkPhone")
    @ResponseBody
    public String  checkPhone(@RequestBody CheckPhoneRequest checkPhoneRequest){
        Map<String,Object> bodyMap = new HashMap<>();
        bodyMap.put("phone",checkPhoneRequest.getPhone());
        return responseResult(Constants.USER_CHECKPHONE,bodyMap,Constants.ESB);
    }
}