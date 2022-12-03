package com.dyy.crm.controller;

import com.dyy.crm.base.BaseController;
import com.dyy.crm.base.ResultInfo;
import com.dyy.crm.model.TreeModel;
import com.dyy.crm.service.ModuleService;
import com.dyy.crm.vo.Module;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("module")
public class ModuleController extends BaseController {

    @Resource
    private ModuleService moduleService;


    /**
     * 查询所有资源信息
     */
    @RequestMapping("list")
    @ResponseBody
    public List<TreeModel> queryAllModule(Integer roleId){
        return moduleService.queryAllModule(roleId);
    }


    /**
     * 进入授权页面
     */
    @RequestMapping("toAddGrantPage")
    public String toAddGrantPage(Integer id, HttpServletRequest req){
        req.setAttribute("id",id);
        return "role/grant";
    }


    /**
     * 查询资源列表
     * @return
     */
    @RequestMapping("moduleList")
    @ResponseBody
    public Map<String,Object> queryModuleList(){
        return moduleService.queryModuleList();
    }


    /**
     * 进入资源管理页面
     */
    @RequestMapping("index")
    public String index(){
        return "module/module";
    }


    /**
     * 添加资源
     * @param module
     * @return
     */
    @PostMapping("addModule")
    @ResponseBody
    public ResultInfo addModule(Module module){
        moduleService.addModule(module);
        return success("添加资源成功");
    }


    /**
     * 更新资源
     * @param module
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateModule(Module module){
        moduleService.updateModule(module);
        return success("更新资源成功");
    }


    /**
     * 进入资源添加页面
     * @param grade
     * @param parentId
     * @param req
     * @return
     */
    @RequestMapping("toAddModulePage")
    public String toAddModulePage(Integer grade,Integer parentId,HttpServletRequest req){
        req.setAttribute("grade",grade);
        req.setAttribute("parentId",parentId);
        return "module/add";
    }


    /**
     * 进入资源修改页面
     * @param id
     * @param req
     * @return
     */
    @RequestMapping("toUpdateModulePage")
    public String update(Integer id,HttpServletRequest req){
        Module module = moduleService.selectByPrimaryKey(id);
        req.setAttribute("module",module);
        return "module/update";
    }


    /**
     * 删除资源
     */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo delete(Integer id) {
        moduleService.deleteModule(id);
        return success("删除成功");
    }
}
