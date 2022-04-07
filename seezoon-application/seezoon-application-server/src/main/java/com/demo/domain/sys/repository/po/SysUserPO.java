package com.demo.domain.sys.repository.po;


import com.seezoon.mybatis.repository.po.BasePO;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
public class SysUserPO extends BasePO<Integer> {

    // 用户编号
    @NotNull
    private Integer userId;

    // 登录名
    @NotBlank
    @Size(max = 50)
    private String username;

    // 密码
    @NotBlank
    @Size(max = 100)
    private String password;

    // 姓名
    @NotBlank
    @Size(max = 50)
    private String name;

    // 手机
    @NotBlank
    @Size(max = 20)
    private String mobile;

    // 头像
    @Size(max = 100)
    private String photo;

    // 邮件
    @Size(max = 50)
    private String email;

    @Override
    public Integer getId() {
        return userId;
    }

    @Override
    public void setId(Integer userId) {
        this.setId(userId);
        this.userId = userId;
    }
}