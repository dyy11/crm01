package com.dyy.crm.interceptor;

import com.dyy.crm.exceptions.NoLoginException;
import com.dyy.crm.service.UserService;
import com.dyy.crm.utils.LoginUserUtil;
import com.dyy.crm.vo.User;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoLoginInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**
         * 获取当前 cookie ，解析用户 id，如果用户 id 存在，且数据库存在对应的用户记录，放行，否则拦截
         */

        // 获取 cookie 中的用户 id
        Integer id = LoginUserUtil.releaseUserIdFromCookie(request);
        // 判断用户 id 是否为空，且数据库中是否有该用户 id 记录
        User user = userService.selectByPrimaryKey(id);
        if(id == 0 || user == null){
            throw new NoLoginException();
        }
        return true;
    }


}
