package com.edu.bupt.new_account.dao;

import com.edu.bupt.new_account.model.DeviceTokenRelation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeviceTokenRelationMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByUuid(String uuid);

    int insert(DeviceTokenRelation record);

    int insertSelective(DeviceTokenRelation record);

    DeviceTokenRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DeviceTokenRelation record);

    int updateByPrimaryKey(DeviceTokenRelation record);
}