package com.demo.domain.sys.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.seezoon.mybatis.repository.mapper.CrudMapper;
import com.demo.domain.sys.repository.po.SysUserPO;

/**
 * 用户信息
 * @author seezoon-generator 2022年1月28日 上午12:00:52
 */
@Repository
@Mapper
public interface SysUserMapper extends CrudMapper<SysUserPO, Integer> {

}