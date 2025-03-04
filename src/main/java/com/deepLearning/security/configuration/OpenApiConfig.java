package com.deepLearning.security.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.stereotype.Component;

@OpenAPIDefinition(
        info = @Info(
                title = "rest spring boot security system",
                description = """
                            this project contains security system for restfull web application\s
                            using JWT token and supports oAuth2 authentication
                           \s""",
                version = "1.0.0",
                contact = @Contact(
                        name = "Ihor Murashko",
                        email = "i.murashko0911@gmail.com",
                        url = "https://www.linkedin.com/in/ihor-murashko/"
                )
        )
)
public class OpenApiConfig {





}
