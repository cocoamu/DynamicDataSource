package com.cocoamu.dynamicdatasource.context;

import com.cocoamu.dynamicdatasource.enums.DbConnectionTypeEnum;

public class MasterSlaveContextHolder {
    /*
     * 当使用ThreadLocal维护变量时，ThreadLocal为每个使用该变量的线程提供独立的变量副本，
     * 所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
     */
    private static final ThreadLocal<String> ROUTE_KEY = new ThreadLocal<String>();


    /**
     * 绑定当前线程数据源路由的key 使用完成后必须调用removeRouteKey()方法删除
     * @param key 路由key
     */
    public static void setRouteKey(String key) {
        ROUTE_KEY.set(key);
    }

    /**
     * 获取当前线程的数据源路由的key
     * @return 路由key
     */
    public static String getRouteKey() {
        String key =  ROUTE_KEY.get();
        return key == null ? DbConnectionTypeEnum.MASTER.name() : key;
    }

    /**
     * 删除与当前线程绑定的数据源路由的key
     */
    public static void removeRouteKey() {
        ROUTE_KEY.remove();
    }
}