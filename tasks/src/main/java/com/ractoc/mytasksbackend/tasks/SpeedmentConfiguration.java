package com.ractoc.mytasksbackend.tasks;

import com.ractoc.mytasksbackend.tasks.db.TasksApplication;
import com.ractoc.mytasksbackend.tasks.db.TasksApplicationBuilder;
import com.ractoc.mytasksbackend.tasks.db.tasks.my_tasks.task.TaskManager;
import com.speedment.runtime.core.component.transaction.TransactionComponent;
import com.speedment.runtime.core.component.transaction.TransactionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class SpeedmentConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpeedmentConfiguration.class);

    @Value("${dbms.host}")
    private String host;
    @Value("${dbms.port}")
    private int port;
    @Value("${dbms.schema}")
    private String schema;
    @Value("${dbms.username}")
    private String username;
    @Value("${dbms.password}")
    private String password;
    @Value("${dbms.collation}")
    private String collation;
    @Value("${dbms.collation.binary}")
    private String collationBinary;
    @Value("${debug}")
    private boolean debug;

    @Bean
    public TasksApplication getLanguageApplication() {
        if (debug) {
            LOGGER.debug("connection parameters");
            LOGGER.debug("host: {}", host);
            LOGGER.debug("port: {}", port);
            LOGGER.debug("schema: {}", schema);
            LOGGER.debug("collation: {}", collation);
            LOGGER.debug("collationBinary: {}", collationBinary);
            return new TasksApplicationBuilder()
                    .withIpAddress(host)
                    .withPort(port)
                    .withUsername(username)
                    .withPassword(password)
                    .withSchema(schema)
                    .withParam("db.mysql.collationName", collation)
                    .withParam("db.mysql.binaryCollationName", collationBinary)
                    .withLogging(TasksApplicationBuilder.LogType.STREAM)
                    .withLogging(TasksApplicationBuilder.LogType.REMOVE)
                    .withLogging(TasksApplicationBuilder.LogType.PERSIST)
                    .withLogging(TasksApplicationBuilder.LogType.UPDATE)
                    .build();
        }
        return new TasksApplicationBuilder()
                .withIpAddress(host)
                .withPort(port)
                .withUsername(username)
                .withPassword(password)
                .withSchema(schema)
                .withParam("db.mysql.collationName", collation)
                .withParam("db.mysql.binaryCollationName", collationBinary)
                .build();
    }

    @Bean
    public TransactionHandler getTransactionHandler(TasksApplication app) {
        return app.getOrThrow(TransactionComponent.class).createTransactionHandler();
    }

    @Bean
    public TaskManager getRaceManager(TasksApplication app) {
        return app.getOrThrow(TaskManager.class);
    }

    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        return new Jackson2ObjectMapperBuilder().indentOutput(true);
    }
}
