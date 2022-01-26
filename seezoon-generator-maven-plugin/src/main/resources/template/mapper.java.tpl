package ${baseRepositoryPackage}.${moduleName}.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.seezoon.framework.concept.domain.repository.mapper.CrudMapper;
import ${baseRepositoryPackage}.${moduleName}.repository.po.${classNamePO};

/**
 * ${menuName!}
 * @author seezoon-generator ${.now}
 */
@Repository
@Mapper
public interface ${className}Mapper extends CrudMapper<${classNamePO}, ${pkPlan.dataType.javaType()}> {

}