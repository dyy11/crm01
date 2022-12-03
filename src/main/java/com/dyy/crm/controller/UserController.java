package com.dyy.crm.controller;


import com.dyy.crm.base.BaseController;
import com.dyy.crm.base.BaseService;
import com.dyy.crm.base.ResultInfo;
import com.dyy.crm.exceptions.ParamsException;
import com.dyy.crm.model.UserModel;
import com.dyy.crm.query.UserQuery;
import com.dyy.crm.service.UserService;
import com.dyy.crm.utils.LoginUserUtil;
import com.dyy.crm.vo.User;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;


    /**
     * 登录功能
     * @param userName
     * @param userPwd
     * @return
     */
    @PostMapping("login")
    @ResponseBody
    public ResultInfo login(String userName,String userPwd){
        ResultInfo resultInfo = new ResultInfo();

        // 调用 service 层方法
        UserModel userModel = userService.userLogin(userName,userPwd);
        // 设置返回状态对象的信息对象属性
        resultInfo.setResult(userModel);

        return resultInfo;
    }


    /**
     * 用户密码修改功能
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updatePwd(HttpServletRequest req,
                                String oldPwd,String newPwd,String againPwd){
        ResultInfo resultInfo = new ResultInfo();

        // 获取 cookie 中的 userId
        Integer id = LoginUserUtil.releaseUserIdFromCookie(req);
        // 调用 service 层修改用户密码
        userService.updatePassword(id,oldPwd,newPwd,againPwd);

        return resultInfo;
    }

    /**
     * 进入修改密码页面
     * @return
     */
    @RequestMapping("toPasswordPage")
    public String toPasswordPage(){

        return "user/password";
    }


    /**
     * 查询销售人员
     */
    @RequestMapping("querySales")
    @ResponseBody
    public List<Map<String, Object>> querySales(){
        return userService.querySales();
    }


    /**
     * 多条件查询用户
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> listUser(UserQuery userQuery){
        return userService.queryByParamsForTable(userQuery);
    }


    /**
     * 进入用户管理页面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "user/user";
    }


    /**
     * 添加用户
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addUser(User user){
        userService.addUser(user);
        return success("用户添加成功");
    }

    /**
     * 进入用户添加或修改界面
     */
    @RequestMapping("addOrUpdateUserPage")
    public String addOrUpdateUserPage(Integer id,HttpServletRequest req){

        // 如果 id 不为空，则将数据设置到请求域以回显
        if(id != null){
            User user = userService.selectByPrimaryKey(id);
            req.setAttribute("userInfo",user);
        }
        return "user/add_update";
    }


    /**
     * 修改用户
     */
    @PostMapping("updateUser")
    @ResponseBody
    public ResultInfo updateUser(User user){
        userService.updateUser(user);
        return success("用户修改成功");
    }


    /**
     * 用户信息删除
     * @param ids
     * @return
     */
    @PostMapping("remove")
    @ResponseBody
    public ResultInfo delete(Integer[] ids){
        userService.removeByIds(ids);
        return success("用户信息删除成功");
    }

}
