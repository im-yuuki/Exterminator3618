package io.exterminator3618.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerConfiguration {

    @Value("${exterminator3618.port:36018}")
    public int port;

}
