package com.im.phone.server.controller.apply;

import com.im.phone.server.cache.CacheManagerEntity;
import com.im.phone.server.common.Constants;
import com.im.phone.server.controller.BaseControler;
import com.im.phone.server.request.IfSmsCodeRequest;
import com.im.phone.server.request.SendSMSRequest;
import com.im.phone.server.request.UserRegisterRequest;
import com.im.phone.server.util.IpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Api(description = "短信接口")
public class SMSController extends BaseControler {

    /**
     * 校验验证码是否正确
     *
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @PostMapping(value = "ifSmsCode")
    @ApiOperation(value="校验验证码是否正确", notes="用户接口")
    public String  ifSmsCode(@RequestBody IfSmsCodeRequest ifSmsCodeRequest){
        if(cacheManager.isContains("smscode"+ifSmsCodeRequest.getPhone()) == Boolean.FALSE){
            return getBaseResultMaps("2000002","请先获取验证码","");
        }else if(!ifSmsCodeRequest.getSmscode().equals(cacheManager.isContains("smscode"+ifSmsCodeRequest.getPhone()))){
            return getBaseResultMaps("2000003","验证码输入有误，请重新输入","");
        }else{
            return getBaseResultMaps("000000","交易成功","");
        }
    }

    /**
     * 发送短信验证码
     *
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @PostMapping(value = "sendsms")
    @ApiOperation(value="发送短信验证码", notes="用户接口")
    public String  sendSms(@RequestBody SendSMSRequest sendSMSRequest){
        Map<String,Object> bodyMap = new HashMap<>();

        //判断手机号是否为空
        if(StringUtils.isEmpty(sendSMSRequest.getPhone())){
            return getBaseResultMaps("1000011","请输入手机号码","");
        }
        bodyMap.put("phone",sendSMSRequest.getPhone());
        //(int) ((Math.random() * 9 + 1) * 100000);
        String code = "111111";
        bodyMap.put("smscode",code);
        cacheManager.putCache("smscode"+sendSMSRequest.getPhone(),new CacheManagerEntity(code),1000*60);
        return responseResult(Constants.SEND_SMS,bodyMap,Constants.ESBXML);
    }
}