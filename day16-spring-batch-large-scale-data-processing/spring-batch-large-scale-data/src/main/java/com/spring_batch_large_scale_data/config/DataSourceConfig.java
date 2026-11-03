
package com.spring_batch_large_scale_data.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.source")
    public DataSourceProperties sourceDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "sourceDataSource")
    public DataSource sourceDataSource(
            @Qualifier("sourceDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.target")
    public DataSourceProperties targetDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "targetDataSource")
    @Primary
    public DataSource targetDataSource(
            @Qualifier("targetDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean(name = "sourceTransactionManager")
    public PlatformTransactionManager sourceTransactionManager(
            @Qualifier("sourceDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "targetTransactionManager")
    @Primary
    public PlatformTransactionManager targetTransactionManager(
            @Qualifier("targetDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
