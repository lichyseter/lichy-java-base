package com.lichy.spring.security.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 生成一个测试密码
 * @author lichy
 * @since 2021/01/25 01:39
 */
@Component
public class Test implements ApplicationRunner {

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(passwordEncoder.encode("user-client"));
    }
}
