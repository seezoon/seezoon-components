package com.demo.domain.sys.repository.po;

import com.seezoon.mybatis.repository.po.PagePOCondition;
import com.seezoon.mybatis.repository.sort.annotation.SortField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户信息
 *
 * @author seezoon-generator 2022年4月7日 下午3:06:40
 */
@Getter
@Setter
@ToString
@SortField({"username:t.username", "name:t.name", "mobile:t.mobile", "createTime:t.create_time"})
public class SysUserPOCondition extends PagePOCondition {

    /**
     * 登录名
     */
    private String username;
    /**
     * 姓名
     */
    private String name;
    /**
     * 手机
     */
    private String mobile;

}