package com.university.hotel.config;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProjectConfigTest {
    @Test
    public void propertiesTest() throws IOException {
        Map<String,String> appConfig = new HashMap<>();
        File file = new File("./src/main/resources/application.properties");
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String line = reader.readLine();
        while (line != null) {
            String[]strings = line.split("=");
            if(strings.length == 2) {
                appConfig.put(strings[0], strings[1]);
            }
            line = reader.readLine();
        }
        String s = "keycloak.principal-attribute=stas";
        Assert.assertEquals("none",appConfig.get("spring.jpa.hibernate.ddl-auto"));
        Assert.assertEquals("root",appConfig.get("spring.datasource.username"));
        Assert.assertEquals("538734962318",appConfig.get("spring.datasource.password"));
        Assert.assertEquals("true",appConfig.get("spring.mustache.expose-request-attributes"));
        Assert.assertEquals("http://localhost:8180/auth",appConfig.get("keycloak.auth-server-url"));
        Assert.assertEquals("Hotel",appConfig.get("keycloak.realm"));
        Assert.assertEquals("hotel-app",appConfig.get("keycloak.resource"));
        Assert.assertEquals("true",appConfig.get("keycloak.public-client"));
        Assert.assertEquals("stas",appConfig.get("keycloak.principal-attribute"));

    }
}
