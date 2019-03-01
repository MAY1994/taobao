package com.taobao.search.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

    //    var b = "http://localhost:8085/search.html?q=" + encodeURIComponent(document.getElementById(a).value);
    @RequestMapping("search.html")
    public String showSearchPage(@RequestParam("q") Long q) {


        return "search";
    }
}