package com.taobao.controller;


import com.taobao.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
    @Autowired
    TestService testService;

    @RequestMapping("showTime")
    @ResponseBody
    public String showTime() {
        return testService.hello();
    }

}
