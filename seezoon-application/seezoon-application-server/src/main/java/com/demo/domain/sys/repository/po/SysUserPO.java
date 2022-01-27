package com.demo.domain.sys.repository.po;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.seezoon.mybatis.repository.po.BasePO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户信息
 *
 * @author seezoon-generator 2022年1月28日 上午12:00:52
 */
@Getter
@Setter
@ToString
public class SysUserPO extends BasePO<Integer> {

    @NotNull
    private Integer userId;

    @NotBlank
    @Size(max = 50)
    private String username;

    @NotBlank
    @Size(max = 100)
    private String password;

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 20)
    private String mobile;

    @Size(max = 100)
    private String photo;

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