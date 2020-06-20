package com.im.phone.server.controller.page;

import com.alibaba.druid.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class ViewController {

    @RequestMapping(value = "/userAgreement.html")
    public String userAgreement(){
        return "userAgreement";
    }
}
