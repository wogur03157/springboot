package com.example.demo.config;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private int port;

    @Value("${spring.data.mongodb.authentication-database}")
    private String authDatabase;

    @Value("${spring.data.mongodb.username}")
    private String username;

    @Value("${spring.data.mongodb.password}")
    private String password;

    @Bean
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString("mongodb://" + host + ":" + port);
        MongoCredential credential = MongoCredential.createCredential(username, authDatabase, password.toCharArray());
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .credential(credential)
                // 여기서 연결 시간 제한을 설정합니다. (밀리초 단위)
                .applyToSocketSettings(builder -> builder.connectTimeout(500000, TimeUnit.MILLISECONDS)) // 예: 5초로 설정
                .build();
        return MongoClients.create(settings);
    }
}
