#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.interfaces.sys.web;

import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageSerializable;
import ${package}.domain.sys.repository.SysUserRepository;
import ${package}.domain.sys.repository.po.SysUserPO;
import ${package}.domain.sys.repository.po.SysUserPOCondition;
import com.seezoon.web.api.DefaultCodeMsgBundle;
import com.seezoon.web.api.Result;
import com.seezoon.web.controller.BaseController;

import lombok.RequiredArgsConstructor;

/**
 * 用户信息
 * @author seezoon-generator 2022年4月7日 下午3:06:40
 */
@RestController
@RequestMapping("/sys/user")
@RequiredArgsConstructor
public class SysUserController extends BaseController {

    private final SysUserRepository sysUserRepository;

    @GetMapping("/query/{userId}")
    public Result<SysUserPO> query(@PathVariable Integer userId) {
        SysUserPO sysUserPO = sysUserRepository.find(userId);
        return Result.ok(sysUserPO);
    }

    @PostMapping("/query")
    public Result<PageSerializable<SysUserPO>> query(@Valid @RequestBody SysUserPOCondition condition) {
        PageSerializable<SysUserPO> pageSerializable = sysUserRepository.find(condition, condition.getPage(), condition.getPageSize());
        return Result.ok(pageSerializable);
    }

    @PostMapping(value = "/save")
    public Result save(@Valid @RequestBody SysUserPO sysUserPO) {
        int count = sysUserRepository.save(sysUserPO);
        return count == 1 ? Result.SUCCESS : Result.error(DefaultCodeMsgBundle.SAVE_ERROR, count);
    }

    @PostMapping(value = "/update")
    public Result update(@Valid @RequestBody SysUserPO sysUserPO) {
        int count = sysUserRepository.updateSelective(sysUserPO);
        return count == 1 ? Result.SUCCESS : Result.error(DefaultCodeMsgBundle.UPDATE_ERROR, count);
    }

    @PostMapping(value = "/delete")
    public Result delete(@RequestParam Integer userId) {
        int count = sysUserRepository.delete(userId);
        return count == 1 ? Result.SUCCESS : Result.error(DefaultCodeMsgBundle.DELETE_ERROR, count);
    }

    @PostMapping(value = "/check_username")
    public Result<Boolean> checkUsername(@RequestParam(required = false) Integer userId,
        @NotBlank @RequestParam String username) {
        SysUserPO sysUserPO = this.sysUserRepository.findByUsername(username);
                return Result.ok(null == sysUserPO || Objects.equals(sysUserPO.getUserId(), userId));
    }

    @PostMapping(value = "/check_mobile")
    public Result<Boolean> checkMobile(@RequestParam(required = false) Integer userId,
        @NotBlank @RequestParam String mobile) {
        SysUserPO sysUserPO = this.sysUserRepository.findByMobile(mobile);
                return Result.ok(null == sysUserPO || Objects.equals(sysUserPO.getUserId(), userId));
    }

}
