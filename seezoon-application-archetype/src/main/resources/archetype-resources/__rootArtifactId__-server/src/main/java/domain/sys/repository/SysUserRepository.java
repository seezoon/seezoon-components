#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.sys.repository;

import ${package}.domain.sys.repository.mapper.SysUserMapper;
import ${package}.domain.sys.repository.po.SysUserPO;
import ${package}.domain.sys.repository.po.SysUserPOCondition;
import com.seezoon.mybatis.repository.AbstractCrudRepository;
import javax.validation.constraints.NotBlank;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 * 用户信息
 *
 * @author seezoon-generator 2022年4月7日 下午3:06:40
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
