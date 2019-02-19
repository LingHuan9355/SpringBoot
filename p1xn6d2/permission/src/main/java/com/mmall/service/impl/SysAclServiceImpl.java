package com.mmall.service.impl;

import com.google.common.base.Preconditions;
import com.mmall.beans.PageQuery;
import com.mmall.common.RequestHolder;
import com.mmall.beans.PageResult;
import com.mmall.dao.SysAclMapper;
import com.mmall.exception.ParamException;
import com.mmall.model.SysAcl;
import com.mmall.param.AclParam;
import com.mmall.service.SysAclService;
import com.mmall.service.SysLogService;
import com.mmall.util.BeanValidator;
import com.mmall.util.IpUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @ClassName : SysAclServiceImpl Created with IDEA
 * @author:CarlLing
 * @CreateDate : 2019-01-27 16:52
 * @Description : 权限点业务处理
 *
 */
@Service
public class SysAclServiceImpl implements SysAclService {

  @Resource private SysAclMapper sysAclMapper;

  @Resource private SysLogService sysLogService;

  /**
   * 新增权限点
   * @param param
   */
  @Override
  public void save(AclParam param) {
    BeanValidator.check(param);
    if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {
      throw new ParamException("当前权限模块下面存在相同名称的权限点");
    }
    SysAcl acl =
        SysAcl.builder()
            .name(param.getName())
            .aclModuleId(param.getAclModuleId())
            .url(param.getUrl())
            .type(param.getType())
            .status(param.getStatus())
            .seq(param.getSeq())
            .remark(param.getRemark())
            .build();
    acl.setCode(generateCode());
    acl.setOperator(RequestHolder.getCurrentUser().getUsername());
    acl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
    acl.setOperateTime(new Date());

    sysAclMapper.insertSelective(acl);
    sysLogService.saveAclLog(null, acl);
  }

  /**
   * 更新权限点
   * @param param
   */
  @Override
  public void update(AclParam param) {
    BeanValidator.check(param);
    if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {
      throw new ParamException("当前权限模块下面存在相同名称的权限点");
    }

    SysAcl before = sysAclMapper.selectByPrimaryKey(param.getId());
    Preconditions.checkNotNull(before, "待更新的权限点不存在");

    SysAcl after =
        SysAcl.builder()
            .id(param.getId())
            .name(param.getName())
            .aclModuleId(param.getAclModuleId())
            .url(param.getUrl())
            .type(param.getType())
            .status(param.getStatus())
            .seq(param.getSeq())
            .remark(param.getRemark())
            .build();
    after.setOperator(RequestHolder.getCurrentUser().getUsername());
    after.setOperateTime(new Date());
    after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));

    sysAclMapper.updateByPrimaryKeySelective(after);
    sysLogService.saveAclLog(before, after);
  }


  public boolean checkExist(int aclModuleId, String name, Integer id) {
     return sysAclMapper.countByNameAndAclModuleId(aclModuleId, name, id) > 0;
  }

  public String generateCode() {
    // return DateUtil.format(new Date(),"yyyyMMddHHmmss");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    return dateFormat.format(new Date()) + "_" + (int) (Math.random() * 100);
  }

  /**
   * 根据权限模块获取权限点接口
   * @param aclModuleId
   * @param page
   * @return
   */
  @Override
  public PageResult<SysAcl> getPageByAclModuleId(Integer aclModuleId, PageQuery page) {
    BeanValidator.check(page);
    int count = sysAclMapper.countByAclModuleId(aclModuleId);
    if (count > 0) {
      List<SysAcl> aclList = sysAclMapper.getPageByAclModuleId(aclModuleId, page);
      return PageResult.<SysAcl>builder().data(aclList).total(count).build();
    }
    return PageResult.<SysAcl>builder().build();
  }
}
