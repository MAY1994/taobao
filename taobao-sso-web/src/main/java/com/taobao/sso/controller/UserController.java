package com.taobao.sso.controller;

import com.taobao.common.pojo.TaobaoResult;
import com.taobao.common.utils.CookieUtils;
import com.taobao.pojo.TbUser;
import com.taobao.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Value("${TB_TOKEN}")
    String TB_TOKEN_KEY;

    @RequestMapping(value = "/user/check/{param}/{type}", method = RequestMethod.GET)
    @ResponseBody
    public TaobaoResult checkParam(@PathVariable("param") String param,
                                   @PathVariable("type") Integer type) {
        return userService.checkUserParam(param, type);
    }

    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    @ResponseBody
    public TaobaoResult userRegister(TbUser tbUser) {
        return userService.userRegister(tbUser);
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    @ResponseBody
    public TaobaoResult userLogin(HttpServletRequest request, HttpServletResponse response,
                                  TbUser tbUser) {
        TaobaoResult result = userService.userLogin(tbUser);
        if (result.getStatus() == 200) {
            CookieUtils.setCookie(request,response, TB_TOKEN_KEY, (String) result.getData());
        }
        return result;
    }

    @RequestMapping(value = "/user/token/{token}", method = RequestMethod.GET)
    @ResponseBody
    public TaobaoResult queryUserInfo(@PathVariable("token") String token) {
        return userService.queryUserInfoByToken(token);
    }

    @RequestMapping(value = "/user/logout/{token}", method = RequestMethod.GET)
    @ResponseBody
    public TaobaoResult userLogout(@PathVariable("token") String token) {
        return userService.userLogout(token);
    }

}
