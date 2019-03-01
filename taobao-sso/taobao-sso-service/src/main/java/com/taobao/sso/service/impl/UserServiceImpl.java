package com.taobao.sso.service.impl;

import com.taobao.common.pojo.TaobaoResult;
import com.taobao.common.utils.JsonUtils;
import com.taobao.mapper.TbUserMapper;
import com.taobao.pojo.TbUser;
import com.taobao.pojo.TbUserExample;
import com.taobao.sso.service.UserService;
import com.taobao.sso.service.jedis.JedisClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    TbUserMapper tbUserMapper;

    @Autowired
    JedisClient jedisClient;

    @Value("${USER_INFO}")
    private String USER_INFO;

    @Value("${EXPIRE_TIME}")
    private Integer EXPIRE_TIME;


    @Override
    public TaobaoResult checkUserParam(String param, Integer type) {
        TaobaoResult result;
        if (type == null) {
            return TaobaoResult.build(200, "OK", false);
        }

        TbUserExample example = new TbUserExample();
        if (1 == type) {
            example.createCriteria().andUsernameEqualTo(param);
        } else if (2 == type) {
            if (param == null || param.equals("")) {
                return TaobaoResult.ok(true);
            }
            example.createCriteria().andPhoneEqualTo(param);
        } else if (3 == type) {
            if (param == null || param.equals("")) {
                return TaobaoResult.ok(true);
            }
            example.createCriteria().andEmailEqualTo(param);
        } else {
            return TaobaoResult.build(200, "OK", false);
        }

        int res = tbUserMapper.countByExample(example);

        if (res == 0) {
            result = TaobaoResult.build(200, "OK", true);
        } else {
            result = TaobaoResult.build(200, "OK", false);
        }
        return result;
    }

    @Override
    public TaobaoResult userRegister(TbUser tbUser) {
        if (null == tbUser) {
            return TaobaoResult.build(400, "注册失败. 请校验数据后请再提交数据", null);
        }
        if (StringUtils.isBlank(tbUser.getUsername())) {
            return TaobaoResult.build(400, "注册失败. 请输入用户名", null);
        }
        if (StringUtils.isBlank(tbUser.getPassword())) {
            return TaobaoResult.build(400, "注册失败. 请输入密码", null);
        }

        TaobaoResult userName = checkUserParam(tbUser.getUsername(), 1);
        TaobaoResult userPhone = checkUserParam(tbUser.getPhone(), 2);
        TaobaoResult userEmail = checkUserParam(tbUser.getEmail(), 3);

        if (!(boolean) userName.getData()) {
            return TaobaoResult.build(400, "注册失败. 用户名冲突", null);
        }
        if (!(boolean) userPhone.getData()) {
            return TaobaoResult.build(400, "注册失败. 手机号已注册", null);
        }
        if (!(boolean) userEmail.getData()) {
            return TaobaoResult.build(400, "注册失败. 邮箱已注册", null);
        }

        /*对用户密码进行加密*/
        String encryptedPassword = DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
        tbUser.setPassword(encryptedPassword);
        tbUser.setCreated(new Date());
        tbUser.setUpdated(tbUser.getCreated());
        int res = tbUserMapper.insertSelective(tbUser);
        if (res == 0) {
            return TaobaoResult.build(400, "注册失败. 请校验数据后请再提交数据", null);
        } else {
            return TaobaoResult.build(200, "OK", null);
        }
    }

    @Override
    public TaobaoResult userLogin(TbUser tbUser) {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        if (null == tbUser || null == tbUser.getUsername()) {
            return TaobaoResult.build(400, "用户名密码错误", null);
        }
        criteria.andUsernameEqualTo(tbUser.getUsername());
        List<TbUser> tbUsers = tbUserMapper.selectByExample(example);
        if (tbUsers == null || tbUsers.size() == 0) {
            return TaobaoResult.build(400, "用户名密码错误", null);
        }
        /*校验密码*/
        TbUser userInDb = tbUsers.get(0);
        if (StringUtils.isEmpty(tbUser.getPassword())) {
            return TaobaoResult.build(400, "用户名密码错误", null);
        }
        String inputPassword = DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
        if (!inputPassword.equals(userInDb.getPassword())) {
            return TaobaoResult.build(400, "用户名密码错误", null);
        }
        //创建token,
        // key：token
        // value：tbUser
        // 存储在Redis中
        String uuid = UUID.randomUUID().toString();
        String token = uuid.replaceAll("-", "");
        jedisClient.set(USER_INFO+":" +token, JsonUtils.objectToJson(userInDb));
        /*设置有效期*/
        jedisClient.expire(USER_INFO+":" + token, EXPIRE_TIME);

        return TaobaoResult.ok(token);
    }

    @Override
    public TaobaoResult queryUserInfoByToken(String token) {
        String userJsonStr = jedisClient.get(USER_INFO+":" + token);
        if (StringUtils.isBlank(userJsonStr)) {
            return TaobaoResult.build(400, "错误信息", null);
        } else {
            jedisClient.expire(USER_INFO + ":" + token, EXPIRE_TIME);
            TbUser tbUser = JsonUtils.jsonToPojo(userJsonStr, TbUser.class);
            return TaobaoResult.ok(tbUser);
        }
    }

    @Override
    public TaobaoResult userLogout(String token) {
        jedisClient.del(USER_INFO+":" + token);
        return TaobaoResult.ok("");
    }
}
