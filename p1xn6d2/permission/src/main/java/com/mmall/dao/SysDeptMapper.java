package com.mmall.dao;

import com.mmall.model.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysDeptMapper {
    int deleteByPrimaryKey(@Param("id")Integer id);

    int insert(SysDept record);

    int insertSelective(SysDept record);

    SysDept selectByPrimaryKey(@Param("id")Integer id);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);

    // �����б�
    List<SysDept> getAllDept();

    //��ȡ������x��ͷ�����в��� xx .%
    List<SysDept> getChildDeptListByLevel(@Param("level") String level);

    //�����������е��Ӳ���
    void  batchUpdateLevel(@Param("sysDeptList") List<SysDept> sysDeptList);

    //��������Ƿ����ظ�
    int countByNameAndParentId(@Param("parentId") Integer parentId, @Param("name") String name, @Param("id") Integer id);


}