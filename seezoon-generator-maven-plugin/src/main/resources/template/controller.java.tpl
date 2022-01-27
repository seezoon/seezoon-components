package ${baseControllerPackage}.${moduleName}.web;
<#list columnPlans as columnPlan>
  <#if columnPlan.uniqueField>

import java.util.Objects;
    <#break>
  </#if>
</#list>
import javax.validation.Valid;
<#list columnPlans as columnPlan>
  <#if columnPlan.uniqueField && columnPlan.stringType>
import javax.validation.constraints.NotBlank;
    <#break>
  </#if>
</#list>
<#list columnPlans as columnPlan>
  <#if columnPlan.uniqueField && !columnPlan.stringType>
import javax.validation.constraints.NotNull;
    <#break>
  </#if>
</#list>

import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageSerializable;
import ${baseRepositoryPackage}.${moduleName}.repository.${className}Repository;
import ${baseRepositoryPackage}.${moduleName}.repository.po.${classNamePO};
import ${baseRepositoryPackage}.${moduleName}.repository.po.${classNamePO}Condition;
import com.seezoon.web.api.DefaultCodeMsgBundle;
import com.seezoon.web.api.Result;
import com.seezoon.web.controller.BaseController;

import lombok.RequiredArgsConstructor;

/**
 * ${menuName!}
 * @author seezoon-generator ${.now}
 */
@RestController
@RequestMapping("/${moduleName}/${functionName}")
@RequiredArgsConstructor
public class ${className}Controller extends BaseController {

    private final ${className}Repository ${className?uncap_first}Repository;

    @GetMapping("/query/{${pkPlan.javaFieldName}}")
    public Result<${classNamePO}> query(@PathVariable ${pkPlan.dataType.javaType()} ${pkPlan.javaFieldName}) {
        ${classNamePO} ${classNamePO?uncap_first} = ${className?uncap_first}Repository.find(${pkPlan.javaFieldName});
        return Result.ok(${classNamePO?uncap_first});
    }

    @PostMapping("/query")
    public Result<PageSerializable<${classNamePO}>> query(@Valid @RequestBody ${classNamePO}Condition condition) {
        PageSerializable<${classNamePO}> pageSerializable = ${className?uncap_first}Repository.find(condition, condition.getPage(), condition.getPageSize());
        return Result.ok(pageSerializable);
    }

    @PostMapping(value = "/save")
    public Result save(@Valid @RequestBody ${classNamePO} ${classNamePO?uncap_first}) {
        int count = ${className?uncap_first}Repository.save(${classNamePO?uncap_first});
        return count == 1 ? Result.SUCCESS : Result.error(DefaultCodeMsgBundle.SAVE_ERROR, count);
    }

    @PostMapping(value = "/update")
    public Result update(@Valid @RequestBody ${classNamePO} ${classNamePO?uncap_first}) {
        int count = ${className?uncap_first}Repository.updateSelective(${classNamePO?uncap_first});
        return count == 1 ? Result.SUCCESS : Result.error(DefaultCodeMsgBundle.UPDATE_ERROR, count);
    }

    @PostMapping(value = "/delete")
    public Result delete(@RequestParam ${pkPlan.dataType.javaType()} ${pkPlan.javaFieldName}) {
        int count = ${className?uncap_first}Repository.delete(${pkPlan.javaFieldName});
        return count == 1 ? Result.SUCCESS : Result.error(DefaultCodeMsgBundle.DELETE_ERROR, count);
    }

    <#list columnPlans as columnPlan>
      <#if columnPlan.uniqueField>
    @PostMapping(value = "/check_${columnPlan.underScoreFieldName}")
    public Result<Boolean> check${columnPlan.javaFieldName?cap_first}(@RequestParam(required = false) ${pkPlan.dataType.javaType()} ${pkPlan.javaFieldName},
        ${columnPlan.stringType?string("@NotBlank","@NotNull")} @RequestParam ${columnPlan.dataType.javaType()} ${columnPlan.javaFieldName}) {
        ${classNamePO} ${classNamePO?uncap_first} = this.${className?uncap_first}Repository.findBy${columnPlan.javaFieldName?cap_first}(${columnPlan.javaFieldName});
                return Result.ok(null == ${classNamePO?uncap_first} || Objects.equals(${classNamePO?uncap_first}.get${pkPlan.javaFieldName?cap_first}(), ${pkPlan.javaFieldName}));
    }

      </#if>
    </#list>
}
