package cn.mic.cloud.code.generate.core;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.mic.cloud.code.generate.config.CodeGenerateConfig;
import cn.mic.cloud.code.generate.util.CodeGenerateUtil;
import cn.mic.cloud.code.generate.vo.ColumnClass;
import cn.mic.cloud.code.generate.vo.TableClass;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author : YangQingJian
 * @date : 2023/1/4
 */
@Slf4j
@RequiredArgsConstructor
public class CodeGenerateCore {

    private final CodeGenerateConfig codeGenerateConfig;

    public void startGenerate(String tableName) {
        beforeStartGenerate();
        log.info("tableName={}, startGenerate", tableName);
        ResultSet resultSet = null;
        Connection connection = null;
        try {
            connection = CodeGenerateUtil.getConnection(codeGenerateConfig);
            TableClass tableClass = CodeGenerateUtil.getTableClass(tableName, connection);
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            resultSet = databaseMetaData.getColumns(connection.getCatalog(), "%", tableClass.getTableNameUpper(), "%");
            List<ColumnClass> allColumnClass = CodeGenerateUtil.getAllColumnClass(resultSet);
            Map<String, Object> dataMap = getDataMap(codeGenerateConfig, tableClass, allColumnClass);
            CodeGenerateRequest codeGenerateRequest = CodeGenerateRequest.builder().dataMap(dataMap).tableClass(tableClass).allColumnClass(allColumnClass).build();
            /**
             * 生成具体的文件
             */
            generateModel(codeGenerateRequest);
            generateApi(codeGenerateRequest);
            generateFeign(codeGenerateRequest);
            generateService(codeGenerateRequest);
            generateServiceImpl(codeGenerateRequest);
            generateRepository(codeGenerateRequest);
            generateRepositoryImpl(codeGenerateRequest);
            generateMapper(codeGenerateRequest);
            generateWeb(codeGenerateRequest);
        } catch (Exception e) {
            log.error("错误信息={}", e.getMessage());
        } finally {
            IoUtil.close(resultSet);
            IoUtil.close(connection);
        }
        log.info("tableName = {} , Generate end", tableName);
    }

    private void beforeStartGenerate() {
        String basePath = codeGenerateConfig.getBasePath();
        Assert.notNull(basePath , "主目录不能为空");
        File file = new File(basePath);
        if (!file.exists()){
            file.mkdirs();
        }
    }

    private void generateWeb(CodeGenerateRequest request) {
        String templateName = "Controller.ftl";
        String suffix = "Controller.java";
        String path = codeGenerateConfig.getFullWebPackagePath() + request.getTableClass().getTableNameFirstUpper() + suffix;
        File modelFile = new File(path);
        CodeGenerateUtil.generateFileByTemplate(templateName, modelFile, request.getDataMap());
    }

    private void generateMapper(CodeGenerateRequest request) {
        String templateName = "Mapper.ftl";
        String suffix = "Mapper.java";
        String path = codeGenerateConfig.getFullMapperPackagePath() + request.getTableClass().getTableNameFirstUpper() + suffix;
        File modelFile = new File(path);
        CodeGenerateUtil.generateFileByTemplate(templateName, modelFile, request.getDataMap());
    }

    private void generateModel(CodeGenerateRequest request) {
        String templateName = "Model.ftl";
        String suffix = ".java";
        String path = codeGenerateConfig.getFullDomainPackagePath() + request.getTableClass().getTableNameFirstUpper() + suffix;
        File modelFile = new File(path);
        CodeGenerateUtil.generateFileByTemplate(templateName, modelFile, request.getDataMap());
    }

    private void generateApi(CodeGenerateRequest request) {
        String templateName = "Api.ftl";
        String suffix = "Api.java";
        String path = codeGenerateConfig.getFullApiPackagePath() + request.getTableClass().getTableNameFirstUpper() + suffix;
        File modelFile = new File(path);
        CodeGenerateUtil.generateFileByTemplate(templateName, modelFile, request.getDataMap());
    }

    private void generateService(CodeGenerateRequest request) {
        String templateName = "Service.ftl";
        String suffix = "Service.java";
        String path = codeGenerateConfig.getFullServicePackagePath() + request.getTableClass().getTableNameFirstUpper() + suffix;
        File modelFile = new File(path);
        CodeGenerateUtil.generateFileByTemplate(templateName, modelFile, request.getDataMap());
    }

    private void generateRepository(CodeGenerateRequest request) {
        String templateName = "Repository.ftl";
        String suffix = "Repository.java";
        String path = codeGenerateConfig.getFullRepositoryPackagePath() + request.getTableClass().getTableNameFirstUpper() + suffix;
        File modelFile = new File(path);
        CodeGenerateUtil.generateFileByTemplate(templateName, modelFile, request.getDataMap());
    }

    private void generateRepositoryImpl(CodeGenerateRequest request) {
        String templateName = "RepositoryImpl.ftl";
        String suffix = "RepositoryImpl.java";
        String path = codeGenerateConfig.getFullRepositoryImplPackagePath() + request.getTableClass().getTableNameFirstUpper() + suffix;
        File modelFile = new File(path);
        CodeGenerateUtil.generateFileByTemplate(templateName, modelFile, request.getDataMap());
    }

    private void generateServiceImpl(CodeGenerateRequest request) {
        String templateName = "ServiceImpl.ftl";
        String suffix = "ServiceImpl.java";
        String path = codeGenerateConfig.getFullServiceImplPackagePath() + request.getTableClass().getTableNameFirstUpper() + suffix;
        File modelFile = new File(path);
        CodeGenerateUtil.generateFileByTemplate(templateName, modelFile, request.getDataMap());
    }

    private void generateFeign(CodeGenerateRequest request) {
        String templateName = "Feign.ftl";
        String suffix = "Feign.java";
        String path = codeGenerateConfig.getFullFeignPackagePath() + request.getTableClass().getTableNameFirstUpper() + suffix;
        File modelFile = new File(path);
        CodeGenerateUtil.generateFileByTemplate(templateName, modelFile, request.getDataMap());
    }

    private Map<String, Object> getDataMap(CodeGenerateConfig codeGenerateConfig, TableClass tableClass, List<ColumnClass> allColumnClass) {
        Map<String, Object> dataMap = Maps.newHashMap();
        CodeGenerateUtil.parseMap(codeGenerateConfig, dataMap);
        CodeGenerateUtil.parseMap(tableClass, dataMap);
        dataMap.put("excludeBaseModelColumns", filterBaseColumn(allColumnClass));
        dataMap.put("allModelColumns", allColumnClass);
        return dataMap;
    }

    private List<ColumnClass> filterBaseColumn(List<ColumnClass> allColumnClass) {
        if (ObjectUtil.isEmpty(allColumnClass)) {
            return Lists.newArrayList();
        }
        Set<String> set = Sets.newHashSet("ID", "CREATED_TIME", "CREATED_BY", "MODIFIED_BY", "IS_DELETED", "LAST_UPDATED_TIME");
        return allColumnClass.stream().filter(temp -> !set.contains(temp.getColumnName().toUpperCase())).collect(Collectors.toList());
    }


    @AllArgsConstructor
    @Builder
    @Data
    static class CodeGenerateRequest {

        private TableClass tableClass;

        private List<ColumnClass> allColumnClass;

        Map<String, Object> dataMap;
    }


}
