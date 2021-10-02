package com.cocoamu.dynamicdatasource.datasource;

import com.cocoamu.dynamicdatasource.context.MasterSlaveContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        /*
         * DynamicDataSourceContextHolder代码中使用setDataSourceType
         * 设置当前的数据源，在路由类中使用getDataSourceType进行获取，
         * 交给AbstractRoutingDataSource进行注入使用。如果为空则默认使用master
         */
        String routeKey = MasterSlaveContextHolder.getRouteKey();
        log.info("当前数据源是：{}", routeKey);
        return routeKey;
    }
}