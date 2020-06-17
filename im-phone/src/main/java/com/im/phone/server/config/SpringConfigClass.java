package com.im.phone.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(locations = {"classpath:dispatcher-servlet.xml"})
public class SpringConfigClass {
}
