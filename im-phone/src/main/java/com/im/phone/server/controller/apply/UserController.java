package com.im.phone.server.controller.apply;

import com.alibaba.fastjson.JSONObject;
import com.im.phone.server.cache.CacheManagerEntity;
import com.im.phone.server.common.Constants;
import com.im.phone.server.controller.BaseControler;
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

        String resultStr = responseResult(Constants.USER_LOGIN,bodyMap,Constants.ESBXML);
        log.info("返回的数据为："+resultStr);
        JSONObject obj = JSONObject.parseObject(resultStr);
        String  resCode = (String)obj.get("resCode");
        if(StringUtils.equals(resCode,"SYS_000000")){
            Object str = obj.get("resData");
            cacheManager.putCache(param.getPhone(),new CacheManagerEntity(str));
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
    @ApiOperation(value="用户注册", notes="用户接口")
    public String  toRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String,Object> bodyMap = new HashMap<>();
        bodyMap.put("ip", IpUtils.getIpAddr(request));
        bodyMap.put("password",userRegisterRequest.getPassword());
        bodyMap.put("phone",userRegisterRequest.getPhone());
        bodyMap.put("smscode",userRegisterRequest.getSmscode());
        return responseResult(Constants.USER_REGISTER,bodyMap,Constants.ESBXML);
    }


    /**
     * 检查手机号是否存在
     *
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @PostMapping(value = "checkPhone")
    @ApiOperation(value="检查手机号是否存在", notes="用户接口")
    public String  checkPhone(@RequestBody CheckPhoneRequest checkPhoneRequest){
        Map<String,Object> bodyMap = new HashMap<>();
        bodyMap.put("phone",checkPhoneRequest.getPhone());
        return responseResult(Constants.USER_CHECKPHONE,bodyMap,Constants.ESBXML);
    }

    /**
     * 用户修改密码
     *
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @PostMapping(value = "updatePassword")
    @ApiOperation(value="用户修改密码", notes="用户接口")
    public String  updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest){
        if(cacheManager.isContains(updatePasswordRequest.getPhone()) == Boolean.FALSE){
            return getBaseResultMaps("2000001","请先登录","");
        }
        String str = String.valueOf(cacheManager.getCacheDataByKey(updatePasswordRequest.getPhone()));
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(str);
        HashMap<String, String> maps = JsonObjectToHashMap(jsonObject);

        Map<String,Object> bodyMap = new HashMap<>();
        bodyMap.put("phone",maps.get("imUserPhone"));
        bodyMap.put("password",updatePasswordRequest.getPhone());
        return responseResult(Constants.USER_CHECKPHONE,bodyMap,Constants.ESBXML);
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
        bodyMap.put("phone",sendSMSRequest.getPhone());
        return responseResult(Constants.SEND_SMS,bodyMap,Constants.ESBXML);
    }

    /**
     * 刷新用户信息
     *
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @PostMapping(value = "refresh")
    @ApiOperation(value="刷新用户信息", notes="用户接口")
    public String  refreshUserData(@RequestBody SendSMSRequest sendSMSRequest){
        if(cacheManager.isContains(sendSMSRequest.getPhone()) == Boolean.FALSE){
            return getBaseResultMaps("2000001","请先登录","");
        }
        String str = String.valueOf(cacheManager.getCacheDataByKey(sendSMSRequest.getPhone()));
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(str);
        HashMap<String, String> maps = JsonObjectToHashMap(jsonObject);

        Map<String,Object> bodyMap = new HashMap<>();
        bodyMap.put("userId",maps.get("imUserId"));
        return responseResult(Constants.USER_FINDUSERBYID,bodyMap,Constants.ESBXML);
    }

    /**
     * 用户退出
     *
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @PostMapping(value = "loginOut")
    @ApiOperation(value="用户退出", notes="用户接口")
    public String  loginOut(@RequestBody SendSMSRequest sendSMSRequest){
        cacheManager.clearByKey(sendSMSRequest.getPhone());
        return getBaseResultMaps("000000","交易成功","");
    }
}