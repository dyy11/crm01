package com.dyy.crm.controller;

import com.dyy.crm.base.BaseController;

import com.dyy.crm.service.PermissionService;
import com.dyy.crm.service.UserService;
import com.dyy.crm.utils.LoginUserUtil;
import com.dyy.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class IndexController extends BaseController {

    @Resource
    private UserService userService;

    @Resource
    private PermissionService permissionService;

    /**
     * 系统登录页
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "index";
    }

    // 系统界面欢迎页
    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }
    /**
     * 后端管理主页面
     * @return
     */
    @RequestMapping("main")
    public String main(HttpServletRequest req){
        // 获取cookie 中的用户 id
        Integer id = LoginUserUtil.releaseUserIdFromCookie(req);
        // 查询用户对象，设置 session 作用域
        User user = userService.selectByPrimaryKey(id);
        req.getSession().setAttribute("user",user);

        //根据当前用户 id 查询其拥有的权限
        List<String> permissions = permissionService.queryAllById(id);
        //将权限资源设置到作用域中
        req.getSession().setAttribute("permissions",permissions);

        return "main";
    }


}
