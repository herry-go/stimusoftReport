package com.report.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class TestController {
    @RequestMapping("hello")
    @ResponseBody
    public String TestHello(){
        return "hello world";
    }
}
