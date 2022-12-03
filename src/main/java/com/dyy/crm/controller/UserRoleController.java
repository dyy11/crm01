package com.dyy.crm.controller;

import com.dyy.crm.base.BaseController;
import com.dyy.crm.service.UserRoleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.annotation.Resource;


@Controller
@RequestMapping("userRole")
public class UserRoleController extends BaseController {

    @Resource
    private UserRoleService userRoleService;


}
