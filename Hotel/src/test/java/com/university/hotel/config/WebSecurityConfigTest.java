package com.university.hotel.config;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public class WebSecurityConfigTest {
    @Autowired
    private HttpSecurity httpSecurity;

    @Test(expected = NullPointerException.class)
    public void configTest() throws Exception {
        WebSecurityConfig webSecurityConfig = new WebSecurityConfig();
        webSecurityConfig.configure(httpSecurity);
    }
}
