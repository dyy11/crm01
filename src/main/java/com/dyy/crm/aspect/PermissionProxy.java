package com.dyy.crm.aspect;


import com.dyy.crm.annoation.RequiredPermission;
import com.dyy.crm.exceptions.AuthException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

// 切面
@Component
@Aspect
public class PermissionProxy {

    @Resource
    private HttpSession session;


    /**
     * 切面会拦截注定包下的指定注解，该定义指拦截该包下的所有注解
     * @param p
     * @return
     */
    @Around(value = "@annotation(com.dyy.crm.annoation.RequiredPermission)")
    public Object around(ProceedingJoinPoint p) throws Throwable {
        Object obj = null;
        // 从 session 中得到当前登录用户所拥有的权限
        List<String> permissions = (List<String>) session.getAttribute("permissions");
        // 判断用户是否拥有权限
        if(permissions == null || permissions.size() < 1){
            // 抛出权限异常
            throw new AuthException();
        }

        // 得到对应的目标方法
        MethodSignature methodSignature = (MethodSignature) p.getSignature();
        // 得到目标方法上的注解
        RequiredPermission requiredPermission = methodSignature.getMethod().getDeclaredAnnotation(RequiredPermission.class);
        // 判断注解上对应的状态码
        if(!permissions.contains(requiredPermission.code())){
            // 如果权限中不包含当前方法上指定的权限码，则抛出异常
            throw new AuthException();
        }
        obj = p.proceed();
        return obj;
    }
}
