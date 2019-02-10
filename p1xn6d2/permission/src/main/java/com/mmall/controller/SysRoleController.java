package com.mmall.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mmall.common.JsonData;
import com.mmall.model.SysUser;
import com.mmall.param.RoleParam;
import com.mmall.service.*;
import com.mmall.util.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName : SysRoleController Created with IDEA
 *
 * @author:CarlLing @CreateDate : 2019-01-27 22:36 @Description : 角色管理
 */
@Controller
@RequestMapping("/sys/role")
public class SysRoleController {

  @Resource private SysRoleService sysRoleService;
  @Resource private SysTreeService sysTreeService;
  @Resource private SysRoleAclService sysRoleAclService;
  @Resource private SysRoleUserService sysRoleUserService;
  @Resource private SysUserService sysUserService;

  @RequestMapping("role.page")
  public ModelAndView page() {
    return new ModelAndView("role");
  }

  @RequestMapping("/save.json")
  @ResponseBody
  public JsonData saveRole(RoleParam param) {
    sysRoleService.save(param);
    return JsonData.success();
  }

  @RequestMapping("/update.json")
  @ResponseBody
  public JsonData updateRole(RoleParam param) {
    sysRoleService.update(param);
    return JsonData.success();
  }

  @RequestMapping("/list.json")
  @ResponseBody
  public JsonData list() {
    return JsonData.success(sysRoleService.getAll());
  }

    /**
     * 角色的权限树
     * @param roleId
     * @return
     */
  @RequestMapping("/roleTree.json")
  @ResponseBody
  public JsonData roleTree(@RequestParam("roleId") int roleId) {
    return JsonData.success(sysTreeService.roleTree(roleId));
  }

  @RequestMapping("/changeAcls.json")
  @ResponseBody
  public JsonData changeAcls(
      @RequestParam("roleId") int roleId,
      @RequestParam(value = "aclIds", required = false, defaultValue = "") String aclIds) {
    List<Integer> aclIdList = StringUtil.splitToListInt(aclIds);
    sysRoleAclService.changeRoleAcls(roleId, aclIdList);
    return JsonData.success();
  }

  @RequestMapping("/changeUsers.json")
  @ResponseBody
  public JsonData changeUsers(
      @RequestParam("roleId") int roleId,
      @RequestParam(value = "userIds", required = false, defaultValue = "") String userIds) {
    List<Integer> userIdList = StringUtil.splitToListInt(userIds);
    sysRoleUserService.changeRoleUsers(roleId, userIdList);
    return JsonData.success();
  }

  @RequestMapping("/users.json")
  @ResponseBody
  public JsonData users(@RequestParam("roleId") int roleId) {
    //查询获取已选用户的列表
    List<SysUser> selectedUserList = sysRoleUserService.getListByRoleId(roleId);
    //获取所有用户的列表
    List<SysUser> allUserList = sysUserService.getAll();
    //未选择的用户列表
    List<SysUser> unselectedUserList = Lists.newArrayList();

    Set<Integer> selectedUserIdSet =
        selectedUserList.stream().map(sysUser -> sysUser.getId()).collect(Collectors.toSet());
    for (SysUser sysUser : allUserList) {
      //判断是否是正常的用户，是否包含在已选的用户中
      if (sysUser.getStatus() == 1 && !selectedUserIdSet.contains(sysUser.getId())) {
        //添加到未选的用户列表中
        unselectedUserList.add(sysUser);
      }
    }
    //不选择1的过滤操作
    // selectedUserList = selectedUserList.stream().filter(sysUser -> sysUser.getStatus() !=
    // 1).collect(Collectors.toList());
    Map<String, List<SysUser>> map = Maps.newHashMap();
    map.put("selected", selectedUserList);
    map.put("unselected", unselectedUserList);
    return JsonData.success(map);
  }
}