package com.dyy.crm.controller;

import com.dyy.crm.base.BaseController;
import com.dyy.crm.base.ResultInfo;
import com.dyy.crm.query.RoleQuery;
import com.dyy.crm.service.RoleService;
import com.dyy.crm.vo.Role;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController extends BaseController {

    @Resource
    private RoleService roleService;

    /**
     * 进入角色管理主页面
     */
    @RequestMapping("index")
    public String index(){
        return "role/role";
    }

    /**
     * 查询所有角色信息
     * @param id
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public List<Map<String,Object>> list(Integer id){
        return roleService.queryAllRole(id);
    }

    /**
     * 多条件查询角色信息
     */
    @RequestMapping("query")
    @ResponseBody
    public Map<String,Object> queryByParams(RoleQuery roleQuery){
        return roleService.queryByParamsForTable(roleQuery);
    }

    /**
     * 打开新增或者修改角色页面
     * @param id
     * @param req
     * @return
     */
    @RequestMapping("addOrUpdateRolePage")
    public String addOrUpdateRolePage(Integer id,HttpServletRequest req){
        // 不为空修改操作
        if(id != null){
            Role role = roleService.selectByPrimaryKey(id);
            req.setAttribute("role",role);
        }
        return "role/add_update";
    }


    /**
     * 新增角色操作
     */
    @PostMapping("save")
    @ResponseBody
    public ResultInfo saveRole(Role role){
        roleService.insertRole(role);
        return success("角色新增成功");
    }

    /**
     * 更新角色操作
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateRole(Role role){
        roleService.updateRole(role);
        return success("角色更新成功");
    }

    /**
     * 删除角色操作
     */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteRole(Integer id){
        roleService.deleteRole(id);
        return success("角色删除成功");
    }


    /**
     * 授权操作
     * @param mIds
     * @param roleId
     * @return
     */
    @PostMapping("addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer[] mIds,Integer roleId){
        roleService.addGrant(mIds,roleId);
        return success("授权成功");
    }
}
