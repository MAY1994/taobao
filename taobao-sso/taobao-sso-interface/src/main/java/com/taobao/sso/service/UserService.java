package com.taobao.sso.service;

import com.taobao.common.pojo.TaobaoResult;
import com.taobao.pojo.TbUser;

public interface UserService {

    TaobaoResult checkUserParam(String param, Integer type);

    TaobaoResult userRegister(TbUser tbUser);

    TaobaoResult userLogin(TbUser tbUser);

    TaobaoResult queryUserInfoByToken(String token);

    TaobaoResult userLogout(String token);
}
