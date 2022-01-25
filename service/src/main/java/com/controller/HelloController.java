package com.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/1/25 7:05 下午
 * description：
 */
@RestController("/hello")
public class HelloController {

    @GetMapping("/say/hello")
    public String sayHello(){
        return "hello";
    }

}
