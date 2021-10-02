package com.cocoamu.dynamicdatasource.config;

import com.cocoamu.dynamicdatasource.datasource.DynamicDataSource;
import com.cocoamu.dynamicdatasource.enums.DbConnectionTypeEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
//@PropertySource("classpath:jdbc.properties")
public class DataSourceConfiguration {
    // 主库数据源
    @Bean
    @ConfigurationProperties("spring.datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().build();
    }

    // 从库数据源
    @Bean
    @ConfigurationProperties("spring.datasource.slave")
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create().build();
    }

    // 数据源路由
    @Bean
    @Primary
    public DataSource dynamicDatasource() {
        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(DbConnectionTypeEnum.MASTER.name(), masterDataSource());
        dataSourceMap.put(DbConnectionTypeEnum.SLAVE.name(), slaveDataSource());
        DynamicDataSource dds = new DynamicDataSource();
        dds.setTargetDataSources(dataSourceMap);
        dds.setDefaultTargetDataSource(masterDataSource());
        return dds;
    }
}