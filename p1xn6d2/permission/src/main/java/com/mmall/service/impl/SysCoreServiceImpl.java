package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.beans.CacheKeyConstants;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysAclMapper;
import com.mmall.dao.SysRoleAclMapper;
import com.mmall.dao.SysRoleUserMapper;
import com.mmall.model.SysAcl;
import com.mmall.model.SysUser;
import com.mmall.service.SysCacheService;
import com.mmall.service.SysCoreService;
import com.mmall.util.JsonMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Auth CarlLing
 * @Date 2019/1/31 16:50
 * @Version 1.0
 * @Desc  获取相关权限业务逻辑
 */
@Service
public class SysCoreServiceImpl implements SysCoreService {

    @Resource
    private SysAclMapper sysAclMapper;
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;
    @Resource
    private SysRoleAclMapper sysRoleAclMapper;
    @Resource
    private SysCacheService sysCacheService;


  @Override
  public List<SysAcl> getCurrentUserAclList() {
      int userId = RequestHolder.getCurrentUser().getId();
      return getUserAclList(userId);
  }


    /**
     * 角色已分配的权限点列表
     * @param roleId
     * @return
     */
  @Override
  public List<SysAcl> getRoleAclList(int roleId) {
      List<Integer> aclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.<Integer>newArrayList(roleId));
      if (CollectionUtils.isEmpty(aclIdList)) {
          return Lists.newArrayList();
      }
      return sysAclMapper.getByIdList(aclIdList);
  }


  @Override
  public List<SysAcl> getUserAclList(int userId) {
      if (isSuperAdmin()) {
          return sysAclMapper.getAll();
      }
      //获取多个角色的用户
      List<Integer> userRoleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
      if (CollectionUtils.isEmpty(userRoleIdList)) {
          return Lists.newArrayList();
      }
      //获取多个角色权限点的总和
      List<Integer> userAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(userRoleIdList);
      if (CollectionUtils.isEmpty(userAclIdList)) {
          return Lists.newArrayList();
      }
      return sysAclMapper.getByIdList(userAclIdList);
  }


  @Override
  public boolean hasUrlAcl(String url) {
      if (isSuperAdmin()) {
          return true;
      }
      List<SysAcl> aclList = sysAclMapper.getByUrl(url);
      if (CollectionUtils.isEmpty(aclList)) {
          return true;
      }

      List<SysAcl> userAclList = getCurrentUserAclListFromCache();
      //List<SysAcl> userAclList = getCurrentUserAclList();
      Set<Integer> userAclIdSet = userAclList.stream().map(acl -> acl.getId()).collect(Collectors.toSet());

      boolean hasValidAcl = false;
      // 规则：只要有一个权限点有权限，那么我们就认为有访问权限
      for (SysAcl acl : aclList) {
          // 判断一个用户是否具有某个权限点的访问权限
          if (acl == null || acl.getStatus() != 1) { // 权限点无效
              continue;
          }
          hasValidAcl = true;
          if (userAclIdSet.contains(acl.getId())) {
              return true;
          }
      }
      if (!hasValidAcl) {
          return true;
      }
      return false;
  }

  @Override
  public boolean isSuperAdmin() {
      // 这里是我自己定义了一个假的超级管理员规则，实际中要根据项目进行修改
      // 可以是配置文件获取，可以指定某个用户，也可以指定某个角色
      SysUser sysUser = RequestHolder.getCurrentUser();
      if (sysUser.getMail().contains("admin")) {
          //TODO
          return true;
      }
      return false;
  }

    /**
     * 当前用户的权限缓存
     * @return
     */
  public List<SysAcl> getCurrentUserAclListFromCache() {
    int userId = RequestHolder.getCurrentUser().getId();
    String cacheValue = sysCacheService.getFromCache(CacheKeyConstants.USER_ACLS, String.valueOf(userId));
    if (StringUtils.isBlank(cacheValue)) {
      List<SysAcl> aclList = getCurrentUserAclList();
      if (CollectionUtils.isNotEmpty(aclList)) {
        sysCacheService.saveCache(
            JsonMapper.obj2String(aclList),
            600,
            CacheKeyConstants.USER_ACLS,
            String.valueOf(userId));
      }
      return aclList;
    }
    return JsonMapper.string2Obj(cacheValue, new TypeReference<List<SysAcl>>() {});
  }
}
