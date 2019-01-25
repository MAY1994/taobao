package com.taobao.service.impl;

import com.taobao.mapper.TestMapper;
import com.taobao.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    TestMapper testMapper;

    @Override
    public String hello() {
        return testMapper.hello();
    }
}
