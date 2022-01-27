package ${baseRepositoryPackage}.${moduleName}.repository.po;

<#if importBigDecimal>
import java.math.BigDecimal;
</#if>
<#if importDate>
import java.util.Date;
</#if>

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

<#if importDate>
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
</#if>
import com.seezoon.mybatis.repository.po.BasePO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ${menuName!}
 *
 * @author seezoon-generator ${.now}
 */
@Getter
@Setter
@ToString
public class ${classNamePO} extends BasePO<${pkPlan.dataType.javaType()}> {

<#list columnPlans as columnPlan>
   <#if !columnPlan.defaultField>
      <#if !columnPlan.nullable>
        <#if columnPlan.stringType>
    @NotBlank
        <#else>
    @NotNull
        </#if>
      </#if>
      <#if columnPlan.stringType>
    @Size(max = ${columnPlan.maxLength?c})
      </#if>
        <#if columnPlan.inputType.name() == "DATE">
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
        <#elseif columnPlan.inputType.name() == "DATETIME">
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        </#if>
    private ${columnPlan.dataType.javaType()} ${columnPlan.javaFieldName};

   </#if>
</#list>

<#if !pkPlan.defaultJavaPkName>
    @Override
    public ${pkPlan.dataType.javaType()} getId() {
        return ${pkPlan.javaFieldName};
    }

    @Override
    public void setId(${pkPlan.dataType.javaType()} ${pkPlan.javaFieldName}) {
        this.setId(${pkPlan.javaFieldName});
        this.${pkPlan.javaFieldName} = ${pkPlan.javaFieldName};
    }
</#if>
}