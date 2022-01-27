package com.demo.domain.sys.repository;

import javax.validation.constraints.NotBlank;

import org.springframework.stereotype.Repository;

import com.seezoon.mybatis.repository.AbstractCrudRepository;
import com.demo.domain.sys.repository.mapper.SysUserMapper;
import com.demo.domain.sys.repository.po.SysUserPO;
import com.demo.domain.sys.repository.po.SysUserPOCondition;
import org.springframework.transaction.annotation.Transactional;


/**
 * 用户信息
 * @author seezoon-generator 2022年1月28日 上午12:00:52
 */
@Repository
public class SysUserRepository extends AbstractCrudRepository<SysUserMapper, SysUserPO, Integer> {

    @Transactional(readOnly = true)
    public SysUserPO findByUsername(@NotBlank String username) {
        SysUserPOCondition sysUserPOCondition = new SysUserPOCondition();
        sysUserPOCondition.setUsername(username);
        return this.findOne(sysUserPOCondition);
    }

    @Transactional(readOnly = true)
    public SysUserPO findByMobile(@NotBlank String mobile) {
        SysUserPOCondition sysUserPOCondition = new SysUserPOCondition();
        sysUserPOCondition.setMobile(mobile);
        return this.findOne(sysUserPOCondition);
    }
}
