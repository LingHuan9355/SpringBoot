package com.mmall.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @ClassName : ApplicationContextHelper
 * Created with IDEA
 * @author:CarlLing
 * @CreateDate : 2019-01-12 22:19
 * @Description : 获取 Spring 上下文工具
 */
@Component("applicationContextHelper")
public class ApplicationContextHelper implements ApplicationContextAware{

    private  static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    public static <T> T popBean(Class<T> clazzz){
        if (applicationContext == null){
            return null;
        }
        return applicationContext.getBean(clazzz);
    }

    public static <T> T popBean(String name,Class<T> clazz){
        if (applicationContext == null){
            return  null;
        }
        return  applicationContext.getBean(name,clazz);
    }
}
