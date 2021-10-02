package com.cocoamu.dynamicdatasource.aop;

import com.cocoamu.dynamicdatasource.annotation.DataSourceSelector;
import com.cocoamu.dynamicdatasource.context.MasterSlaveContextHolder;
import com.cocoamu.dynamicdatasource.enums.DbConnectionTypeEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.util.Objects;

@Component
@Aspect
@Order(-1) // 保证该AOP在@Transactional之前执行
public class DynamicDataSourceAspect {

    //拦截DbAnnotation
    @Pointcut("@annotation(com.cocoamu.dynamicdatasource.annotation.DataSourceSelector)")
    public void dataSourcePointCut() {
    }

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //判断事务是否在活跃状态 如果是则使用主库，否则获取注解指定的数据源
        boolean synchronizationActive = TransactionSynchronizationManager.isActualTransactionActive();
        if (synchronizationActive) {
            MasterSlaveContextHolder.setRouteKey(DbConnectionTypeEnum.MASTER.name());
        } else {
            MasterSlaveContextHolder.setRouteKey(this.getDSAnnotation(joinPoint).value().name());
        }
        try {
            return joinPoint.proceed();
        } catch (Exception ex) {
            throw ex;
        } finally {
            MasterSlaveContextHolder.removeRouteKey();
        }
    }

    /**
     * 根据类或方法获取数据源注解
     */
    private DataSourceSelector getDSAnnotation(ProceedingJoinPoint joinPoint) {
        Class<?> targetClass = joinPoint.getTarget().getClass();
        //先判断方法再判断类最后判断父类
        DataSourceSelector dsAnnotation = findMethodOrClassLevelAnnotation(targetClass, DataSourceSelector.class);
        if (Objects.nonNull(dsAnnotation)) {
            return dsAnnotation;
        } else {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            return methodSignature.getMethod().getAnnotation(DataSourceSelector.class);
        }
    }

    /**
     * 查找类以及父类的注解
     * @param handler
     * @param annotationClass
     * @param <A>
     * @return
     */
    private  <A extends Annotation> A findMethodOrClassLevelAnnotation(Object handler, Class<A> annotationClass) {
        if (!(handler instanceof HandlerMethod)) return null;
        A annotationOnMethod = ((HandlerMethod) handler).getMethodAnnotation(annotationClass);
        if (annotationOnMethod != null) return annotationOnMethod;
        Class<?> targetClass = ((HandlerMethod) handler).getBeanType();
        while (true) {
            A annotation = targetClass.getAnnotation(annotationClass);
            if (annotation != null) return annotation;
            targetClass = targetClass.getSuperclass();
            if (Object.class.equals(targetClass)) return null;
        }
    }

}