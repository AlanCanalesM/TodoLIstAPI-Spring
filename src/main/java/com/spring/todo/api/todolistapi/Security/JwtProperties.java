package com.spring.todo.api.todolistapi.Security;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// This class is used to get the jwt properties
@Component
@Data
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    private String secretKey = "rzxlszyykpbgqcflzxsqcysyhljt";

    // validity in milliseconds
    private long validityInMs = 3600000; // 1h

}
