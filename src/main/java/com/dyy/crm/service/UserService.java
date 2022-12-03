package com.dyy.crm.service;


import com.dyy.crm.base.BaseService;
import com.dyy.crm.dao.UserMapper;
import com.dyy.crm.dao.UserRoleMapper;
import com.dyy.crm.model.UserModel;
import com.dyy.crm.utils.AssertUtil;
import com.dyy.crm.utils.Md5Util;
import com.dyy.crm.utils.PhoneUtil;
import com.dyy.crm.utils.UserIDBase64;
import com.dyy.crm.vo.User;
import com.dyy.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.beans.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserService extends BaseService<User, Integer> {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    /**
     * 根据用户姓名查询用户记录
     *
     * @param userName
     * @param userPwd
     * @return
     */
    public UserModel userLogin(String userName, String userPwd) {
        //1. 参数非空判断
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd), "密码不能为空");
        //2. 调用 dao 层方法查询用户记录
        User user = userMapper.queryUserByName(userName);
        //3. 判断查询到的用户是否为空
        AssertUtil.isTrue(null == user, "未查询到该用户");
        //4. 核对用户输入的密码和查询到的密码
        checkUserPwd(userPwd, user.getUserPwd());
        //5. 返回构建的对象
        return buildUserInfo(user);
    }

    /**
     * 修改用户密码
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePassword(Integer id, String oldPwd, String newPwd, String againPwd) {
        // 通过用户 id 查询用户记录
        User user = userMapper.selectByPrimaryKey(id);
        // 判断用户对象是否存在
        AssertUtil.isTrue(user == null, "当前用户不存在");
        // 参数校验
        checkParams(user, oldPwd, newPwd, againPwd);
        // 设置用户的新密码
        user.setUserPwd(Md5Util.encode(newPwd));
        // 执行更新，判断受影响的行数
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1, "用户密码修改失败");
    }

    /**
     * 构建返回给客户端的对象
     *
     * @param user
     * @return
     */
    private UserModel buildUserInfo(User user) {
        UserModel userModel = new UserModel();
        userModel.setUserName(user.getUserName());

//        userModel.setUserId(user.getId());

        //设置加密后的用户 id
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setTrueName(user.getTrueName());

        return userModel;
    }

    /**
     * 查询销售人员
     */
    public List<Map<String, Object>> querySales() {
        return userMapper.querySales();
    }


    /**
     * 添加用户
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user) {
        // 参数校验
        checkUserParams(user);
        // 设置默认值
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        // 设置默认密码 将其加密后存入
        user.setUserPwd(Md5Util.encode("123456"));
        // 执行
        AssertUtil.isTrue(userMapper.insertSelective(user) < 1, "用户添加失败");

        // 用户角色关联，需要两个参数，用户 id，角色 id        user.getId  user.getRoleIds
        relationUserRole(user.getId(), user.getRoleIds());
    }


    /**
     * 用户角色关联
     *
     * @param id
     * @param roleIds
     */
    private void relationUserRole(Integer id, String roleIds) {
        // 通过用户 id 查询角色记录，判断是否存在，
        Integer count = userRoleMapper.countUserRoleByUserId(id);
        // 如果存在就删掉对应的所有角色记录
        if( count > 0){
            AssertUtil.isTrue(userRoleMapper.removeUserRoleByUserId(id) != count,"用户角色数据异常");
        }
        // 接着判断角色 id 是否存在，如果存在添加对应角色
        if(!StringUtils.isBlank(roleIds)){
            // 将前端传来的角色 id 字符串分割成字符数组
            String[] split = roleIds.split(",");
            List<UserRole> userRoles = new ArrayList<>();
            // 遍历数组，将角色信息设置进去
            for(String roleId : split){
                // 设置用户角色信息的默认值
                UserRole userRole = new UserRole();
                userRole.setUserId(id);
                userRole.setRoleId(Integer.parseInt(roleId));
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                // 将用户角色信息设置到集合中
                userRoles.add(userRole);
            }
            // 执行批量添加角色信息操作
            AssertUtil.isTrue(userRoleMapper.insertBatch(userRoles) != userRoles.size(),"用户角色添加异常");
        }
    }


    /**
     * 更新用户
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user) {
        // 判断用户 id 是否为空，且数据是否存在
        AssertUtil.isTrue(user.getId() == null, "用户信息异常");
        AssertUtil.isTrue(userMapper.selectByPrimaryKey(user.getId()) == null, "用户信息异常");
        // 参数校验
        checkUserParams(user);
        // 更新数据
        user.setUpdateDate(new Date());
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1, "用户信息更新失败");

        // 用户角色关联，需要两个参数，用户 id，角色 id        user.getId  user.getRoleIds
        relationUserRole(user.getId(), user.getRoleIds());
    }

    /**
     * 用户信息校验
     *
     * @param user
     */
    private void checkUserParams(User user) {
        // 基本信息的非空判断
        AssertUtil.isTrue(StringUtils.isBlank(user.getUserName()), "用户姓名不可以为空");
        // 根据传进来的姓名查数据库中有没有该数据
        User temp = userMapper.queryUserByName(user.getUserName());

        // 判断数据库里有没有该用户
        // 增加操作     数据库中根据姓名查到数据就不可用
        // 更新操作     数据库中如果没查到数据可用，如果查到数据且查到的 id 和当前前端获得的 id 相同证明可用
        AssertUtil.isTrue(temp != null && !(temp.getId()).equals(user.getId()), "该用户已存在");

        AssertUtil.isTrue(StringUtils.isBlank(user.getEmail()), "用户邮箱不可以为空");
        AssertUtil.isTrue(StringUtils.isBlank(user.getPhone()), "用户联系方式不可以为空");
        // 手机号码格式判断     不符合抛出异常
        AssertUtil.isTrue(!PhoneUtil.isMobile(user.getPhone()), "联系方式格式不正确");
    }

    /**
     * 参数校验
     *
     * @param user
     * @param oldPwd
     * @param newPwd
     * @param againPwd
     */
    private void checkParams(User user, String oldPwd, String newPwd, String againPwd) {
        // 非空校验
        AssertUtil.isTrue(StringUtils.isBlank(oldPwd), "原密码不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(newPwd), "请输入需要修改密码");
        AssertUtil.isTrue(StringUtils.isBlank(againPwd), "请确认修改后的密码");

        // 值的逻辑判断
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPwd)), "原密码错误");
        AssertUtil.isTrue(oldPwd.equals(newPwd), "修改后的密码不能和原密码相同");
        AssertUtil.isTrue(!newPwd.equals(againPwd), "修改密码两次输入不一致");
    }

    /**
     * 未加密密码与加密密码判断
     *
     * @param userPwd
     * @param pwd
     */
    private void checkUserPwd(String userPwd, String pwd) {
        // 将用户传入的密码加密
        userPwd = Md5Util.encode(userPwd);
        // 判断密码是否正确
        AssertUtil.isTrue(!userPwd.equals(pwd), "密码不正确，请重新输入");

    }

    /**
     * 删除用户信息
     *
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeByIds(Integer[] ids) {
        // 判断是否选中数据记录
        AssertUtil.isTrue(ids == null || ids.length < 1, "请选择要删除的数据");

        // 执行删除操作
        AssertUtil.isTrue(userMapper.removeUser(ids) != ids.length, "用户信息删除失败");

        // 使用用户 id 查询所有的角色信息数据
        for (Integer id : ids) {
            Integer count = userRoleMapper.countUserRoleByUserId(id);
            // 如果有数据则删除
            if(count > 0){
                // 删除对应用户 id 的所有的角色信息
                AssertUtil.isTrue(userRoleMapper.deleteBatch(ids) != count,"用户角色信息删除失败");
            }
        }


    }
}
