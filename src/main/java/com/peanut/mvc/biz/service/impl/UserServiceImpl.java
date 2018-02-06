package com.peanut.mvc.biz.service.impl;

import com.peanut.mvc.biz.service.UserService;
import com.peanut.mvc.framework.annotation.Service;

/**
 * @author peanut.huang
 * @date 2018/2/5.
 */
@Service
public class UserServiceImpl implements UserService {

    @Override
    public String test() {
        return "test";
    }
}
