package com.im.phone.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.im.phone.server.cache.Cache;
import com.im.phone.server.common.Constants;
import com.im.phone.server.request.*;
import com.im.phone.server.util.IpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
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

        String resultStr = responseResult(Constants.USER_LOGIN,bodyMap,Constants.ESB);
        log.info("返回的数据为："+resultStr);
        JSONObject obj = JSONObject.parseObject(resultStr);
        Object  str = null;
        String  resCode = (String)obj.get("resCode");
        if(StringUtils.equals(resCode,"000000")){
            str = obj.get("resData");
            Cache.put("saveUserData",str);
        }
        return resultStr;
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

    /**
     * 用户修改密码
     *
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @PostMapping(value = "updatePassword")
    @ResponseBody
    public String  updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest){
        String str = String.valueOf(Cache.get(updatePasswordRequest.getPhone()));
        if("null".equals(str)){
            return getBaseResultMaps("2000001","请先登录","");
        }
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(str);
        HashMap<String, String> maps = JsonObjectToHashMap(jsonObject);

        Map<String,Object> bodyMap = new HashMap<>();
        bodyMap.put("phone",maps.get("imUserPhone"));
        bodyMap.put("password",updatePasswordRequest.getPhone());
        return responseResult(Constants.USER_CHECKPHONE,bodyMap,Constants.ESB);
    }

    /**
     * 发送短信验证码
     *
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @PostMapping(value = "sendsms")
    @ResponseBody
    public String  sendSms(@RequestBody SendSMSRequest sendSMSRequest){
        HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String,Object> bodyMap = new HashMap<>();
        bodyMap.put("phone",sendSMSRequest.getPhone());
        return responseResult(Constants.SEND_SMS,bodyMap,Constants.ESB);
    }
}