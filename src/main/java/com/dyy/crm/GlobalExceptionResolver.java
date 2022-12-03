package com.dyy.crm;

import com.alibaba.fastjson.JSON;
import com.dyy.crm.base.ResultInfo;
import com.dyy.crm.exceptions.AuthException;
import com.dyy.crm.exceptions.NoLoginException;
import com.dyy.crm.exceptions.ParamsException;
import org.springframework.stereotype.Component;


import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * 全局异常统一处理
 */
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {


    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest,
                                         HttpServletResponse httpServletResponse, Object o, Exception e) {

        ModelAndView mv = new ModelAndView();
        /**
         * 非法请求拦截   判断是否抛出未登录异常     如果抛出异常则重定向到登录页面
         */
        if(e instanceof NoLoginException){
            mv.setViewName("redirect:/index");
            return mv;
        }

        // 默认异常处理(返回类型是视图)
        // 设置异常信息
        mv.setViewName("error");
        mv.addObject("code",400);
        mv.addObject("msg","系统异常，请稍后重试");

        // 判断 o
        if(o instanceof HandlerMethod){
            // 强转
            HandlerMethod handlerMethod = (HandlerMethod) o;
            // 获取方法上的 @ResponseBody 注解对象
            ResponseBody res = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);
            // 判断是否为空,
            if(res == null){
                if(e instanceof ParamsException){
                    ParamsException p = (ParamsException) e;
                    mv.addObject("code",p.getCode());
                    mv.addObject("msg",p.getMsg());
                }else if(e instanceof AuthException){
                    AuthException a = (AuthException) e;
                    mv.addObject("code",a.getCode());
                    mv.addObject("msg",a.getMsg());
                }
                return mv;
            }else{
                // 默认异常处理
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(500);
                resultInfo.setMsg("系统异常，请重试");
                // 判断是否是自定义异常
                if(e instanceof ParamsException){
                    ParamsException p = (ParamsException) e;
                    resultInfo.setCode(p.getCode());
                    resultInfo.setMsg(p.getMsg());
                }else if(e instanceof AuthException){
                    AuthException a = (AuthException) e;
                    resultInfo.setCode(a.getCode());
                    resultInfo.setMsg(a.getMsg());
                }
                // 设置相应类型和编码格式（相应 json 格式的数据）
                httpServletResponse.setContentType("application/json;charset=UTF-8");
                // 得到字符输出流
                PrintWriter writer = null;
                try {
                    writer = httpServletResponse.getWriter();
                    // 将需要返回的的对象转换成 json 格式的字符
                    String json = JSON.toJSONString(resultInfo);
                    // 输出数据
                    writer.write(json);
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    // 释放资源
                    if(writer != null) {
                        writer.close();
                    }
                }
                return null;
            }
        }

        return mv;
    }
}
