package com.retail.insights.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DataSourceConfig {
    @Bean(destroyMethod = "close")
    public HikariDataSource metadataDataSource(
            @Value("${app.pg.url}") String url,
            @Value("${app.pg.user}") String user,
            @Value("${app.pg.password}") String password,
            @Value("${app.pg.pool-size}") int poolSize,
            @Value("${app.connection-timeout-ms}") long timeout) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setMaximumPoolSize(poolSize);
        dataSource.setConnectionTimeout(timeout);
        return dataSource;
    }

    @Bean("metadata")
    public JdbcTemplate metadataJdbcTemplate(
            @Qualifier("metadataDataSource") HikariDataSource dataSource,
            @Value("${app.query-timeout-seconds}") int timeout) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setQueryTimeout(timeout);
        return jdbcTemplate;
    }

    @Bean(destroyMethod = "close")
    public HikariDataSource analyticsDataSource(
            @Value("${app.ch.url}") String url,
            @Value("${app.ch.user}") String user,
            @Value("${app.ch.password}") String password,
            @Value("${app.ch.pool-size}") int poolSize,
            @Value("${app.connection-timeout-ms}") long timeout) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setMaximumPoolSize(poolSize);
        dataSource.setConnectionTimeout(timeout);
        return dataSource;
    }

    @Bean("analytics")
    public JdbcTemplate analyticsJdbcTemplate(
            @Qualifier("analyticsDataSource") HikariDataSource dataSource,
            @Value("${app.query-timeout-seconds}") int timeout) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setQueryTimeout(timeout);
        return jdbcTemplate;
    }

}
