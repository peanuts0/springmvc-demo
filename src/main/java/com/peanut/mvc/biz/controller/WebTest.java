package com.peanut.mvc.biz.controller;

import com.peanut.mvc.biz.service.UserService;
import com.peanut.mvc.framework.annotation.Controller;
import com.peanut.mvc.framework.annotation.RequestMapping;
import com.peanut.mvc.framework.enums.RequestMethod;

import javax.annotation.Resource;

/**
 * @author peanut.huang
 * @date 2018/2/4.
 */
@Controller()
@RequestMapping("/web")
public class WebTest {

    @Resource
    private UserService userService;


    @RequestMapping(value = "test", method = RequestMethod.GET)
    public String requestTest(){

        return userService.test();
    }
}
