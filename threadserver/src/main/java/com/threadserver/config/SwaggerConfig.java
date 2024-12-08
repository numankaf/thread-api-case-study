package com.threadserver.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(servers = { @Server(url = "http://localhost:8080")},
        info = @Info(title = "Thread Api Case Study Server",
                description = "A Rest Api Server generated for Thread Api Case Study",
                version = "${application.version}"))
public class SwaggerConfig {
}
