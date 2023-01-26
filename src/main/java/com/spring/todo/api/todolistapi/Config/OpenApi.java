package com.spring.todo.api.todolistapi.Config;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;

import java.util.Arrays;
import java.util.Collections;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApi {

//     @Bean
//     public OperationCustomizer customize() {
//     return (operation, handlerMethod) -> operation.addParametersItem(
//             new Parameter()
//                     .in("header")
//                     .required(false)
//                     .description("Bearer token")
//                     .name("Authorization"));
// }

 @Bean
 public OpenAPI customOpenAPI() {
   return new OpenAPI().components(new Components().addSecuritySchemes("bearer-key",
     new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
}


}
