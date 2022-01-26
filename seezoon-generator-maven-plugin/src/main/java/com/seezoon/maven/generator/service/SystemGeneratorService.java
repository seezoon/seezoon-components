package com.seezoon.maven.generator.service;

import com.seezoon.maven.generator.dao.GeneratorDao;
import com.seezoon.maven.generator.dto.db.DbTable;
import com.seezoon.maven.generator.dto.db.DbTableColumn;
import com.seezoon.maven.generator.io.FileCodeGenerator;
import com.seezoon.maven.generator.plan.TablePlan;
import com.seezoon.maven.generator.plan.TablePlanHandler;
import com.seezoon.maven.generator.plan.impl.SystemTablePlanHandlerImpl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 系统默认生成方案
 *
 * @author hdf
 */
@Service
@RequiredArgsConstructor
@Validated
public class SystemGeneratorService  {

    private final GeneratorDao generatorDao;
    /**
     * sql mapper 生成所在路径
     */
    @Value("${base.sqlMapper.path}")
    private String baseSqlMapperPath;

    /**
     * repository 生成所在包
     */
    @Value("${base.repository.package}")
    private String baseRepositoryPackage;

    /**
     * controller 生成所在包
     */
    @Value("${base.controller.package}")
    private String baseControllerPackage;

    @Value("${base.dir}")
    private String baseDir;

    @Value("#{'${tables:}'.empty ? null : '${tables:}'.split(',')}")
    private String[] tables;


    public void generate() throws IOException {
        TablePlanHandler tablePlanHandler = new SystemTablePlanHandlerImpl(baseSqlMapperPath, baseRepositoryPackage,
                baseControllerPackage);
        FileCodeGenerator fileCodeGenerator = new FileCodeGenerator(baseDir);

        List<DbTable> allDbTables = new ArrayList<>();
        if (ArrayUtils.isEmpty(tables)) {
            allDbTables = generatorDao.findTable(null);
        } else {
            for (String tableName : tables) {
                allDbTables.add(this.findTable(tableName));
            }
        }
        List<TablePlan> tablePlans = new ArrayList<>();
        allDbTables.forEach((dbTable) -> {
            List<DbTableColumn> dbTableColumns = findDbTableColumns(dbTable.getName());
            TablePlan tablePlan = tablePlanHandler.generate(dbTable, dbTableColumns);
            tablePlans.add(tablePlan);
        });
        fileCodeGenerator.generate(tablePlans.toArray(new TablePlan[tablePlans.size()]));
    }

    public List<DbTableColumn> findDbTableColumns(@NotBlank String tableName) {
        List<DbTableColumn> dbTableColumns = generatorDao.findColumnByTableName(tableName);
        return dbTableColumns;
    }

    public DbTable findTable(@NotBlank String tableName) {
        DbTable dbTable = generatorDao.findTable(tableName).stream().findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("can't find tableName:%s", tableName)));
        return dbTable;
    }
}
