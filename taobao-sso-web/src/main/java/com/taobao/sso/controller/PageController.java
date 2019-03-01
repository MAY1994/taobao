package com.taobao.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @RequestMapping("/page/{page}")
    public String showPage(@PathVariable("page") String page) {
        return page;
    }

}
