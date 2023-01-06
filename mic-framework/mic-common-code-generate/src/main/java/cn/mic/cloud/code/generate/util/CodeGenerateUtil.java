package cn.mic.cloud.code.generate.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.mic.cloud.code.generate.config.CodeGenerateConfig;
import cn.mic.cloud.code.generate.core.FreeMarkerTemplateUtils;
import cn.mic.cloud.code.generate.vo.ColumnClass;
import cn.mic.cloud.code.generate.vo.TableClass;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import freemarker.template.Template;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * @author : YangQingJian
 * @date : 2023/1/5
 */
public class CodeGenerateUtil {

    @SneakyThrows
    public static Connection getConnection(CodeGenerateConfig codeGenerateConfig) {
        Properties prop = new Properties();
        prop.setProperty("user", codeGenerateConfig.getJdbcUser());
        prop.setProperty("password", codeGenerateConfig.getJdbcPwd());
        prop.setProperty("remarks", "true");
        Class.forName(codeGenerateConfig.getJdbcDriver());
        return DriverManager.getConnection(codeGenerateConfig.getJdbcUrl(), prop);
    }

    @SneakyThrows
    public static void generateFileByTemplate(final String templateName, File file, Map<String, Object> dataMap) {
        Template template = FreeMarkerTemplateUtils.getTemplate(templateName);
        File dir = new File(file.getParent());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(file);
        Writer out = new BufferedWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8), 10240);
        template.process(dataMap, out);
    }

    public static String replaceUnderLineAndUpperCase(String str) {
        StringBuffer sb = new StringBuffer();
        sb.append(str);
        int count = sb.indexOf("_");
        while (count != 0) {
            int num = sb.indexOf("_", count);
            count = num + 1;
            if (num != -1) {
                char ss = sb.charAt(count);
                char ia = (char) (ss - 32);
                sb.replace(count, count + 1, ia + "");
            }
        }
        String result = sb.toString().replaceAll("_", "");
        return StringUtils.capitalize(result);
    }

    public static Map<String, List<String>> getJdbcJavaType() {
        Map<String, List<String>> jdbcJavaType = Maps.newHashMap();
        jdbcJavaType.put("String", Lists.newArrayList("BLOB", "CHAR", "JSON", "LINESTRING", "LONGBLOB", "LONGTEXT", "TEXT", "VARCHAR", "TINYTEXT"));
        jdbcJavaType.put("Integer", Lists.newArrayList("INT", "INTEGER", "TINYINT"));
        jdbcJavaType.put("Boolean", Lists.newArrayList("BIT"));
        jdbcJavaType.put("Double", Lists.newArrayList("DOUBLE"));
        jdbcJavaType.put("Float", Lists.newArrayList("FLOAT"));
        jdbcJavaType.put("java.util.Date", Lists.newArrayList("DATETIME", "DATE", "TIME", "TIMESTAMP"));
        jdbcJavaType.put("java.math.BigDecimal", Lists.newArrayList("NUMERIC", "DECIMAL"));
        jdbcJavaType.put("Long", Lists.newArrayList("BIGINT"));
        return jdbcJavaType;
    }

    @SneakyThrows
    public static List<ColumnClass> getAllColumnClass(ResultSet resultSet) {
        List<ColumnClass> allColumnClassList = Lists.newArrayList();
        while (resultSet.next()) {
            ColumnClass columnClass = new ColumnClass();
            //获取字段名称
            columnClass.setColumnName(resultSet.getString("COLUMN_NAME").toLowerCase());
            //获取字段类型
            columnClass.setColumnType(resultSet.getString("TYPE_NAME").toUpperCase());
            for (Map.Entry<String, List<String>> entry : CodeGenerateUtil.getJdbcJavaType().entrySet()) {
                if (entry.getValue().contains(columnClass.getColumnType())) {
                    columnClass.setJavaType(entry.getKey());
                    break;
                }
            }
            if (ObjectUtil.isEmpty(columnClass.getJavaType())) {
                throw new Exception("columnName = " + columnClass.getColumnName() + ",columnType = " + columnClass.getColumnType() + ",未知JAVA数据类型");
            }
            //转换字段名称，如 sys_name 变成 SysName
            columnClass.setChangeColumnName(CodeGenerateUtil.replaceUnderLineAndUpperCase(resultSet.getString("COLUMN_NAME").toLowerCase()));
            //字段在数据库的注释
            columnClass.setColumnComment(resultSet.getString("REMARKS"));
            String tempNullAble = resultSet.getString("NULLABLE");
            columnClass.setNullAble(null != tempNullAble && tempNullAble.equals("0") ? "true" : "false");
            columnClass.setColumnSize(Integer.valueOf(resultSet.getString("COLUMN_SIZE")));
            String tempDecimal = resultSet.getString("DECIMAL_DIGITS");
            columnClass.setDecimalDigits(null != tempDecimal ? Integer.valueOf(resultSet.getString("DECIMAL_DIGITS")) : 0);
            if (ObjectUtil.isEmpty(columnClass.getColumnComment())) {
                throw new Exception("字段【" + columnClass.getColumnName() + "】的备注不能为空，请备注好之后才能生成代码");
            }
            allColumnClassList.add(columnClass);
        }
        return allColumnClassList;
    }

    @SneakyThrows
    public static TableClass getTableClass(String tableName, Connection connection) {
        ResultSet resultSet = null;
        try {
            TableClass tableClass = new TableClass();
            tableClass.setTableName(tableName);
            tableClass.setTableNameUpper(tableName.toUpperCase());
            tableClass.setTableNameFirstUpper(CodeGenerateUtil.replaceUnderLineAndUpperCase(tableName));
            tableClass.setTableNameCame(StrUtil.lowerFirst(tableClass.getTableNameFirstUpper()));
            tableClass.setTableNameLower(tableName.toLowerCase());
            /**
             *
             * 获取备注信息
             */
            String sql = "SELECT * FROM information_schema.`TABLES` where  1=1 AND table_schema ='" + connection.getCatalog() + "' AND  UPPER(table_name)='" + tableName.toUpperCase() + "'";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new Exception(tableName + "查询失败");
            }
            String tableComment = resultSet.getString("TABLE_COMMENT");
            if (ObjectUtil.isNull(tableComment)) {
                throw new Exception("表的名称的备注不允许为空");
            }
            tableClass.setTableComment(tableComment);
            return tableClass;
        } finally {
            IoUtil.close(resultSet);
        }
    }

    /**
     * 注意使用对应的get方法来进行调用
     *
     * @param object
     * @param dataMap
     */
    public static void parseMap(Object object, Map<String, Object> dataMap) {
        PropertyDescriptor[] propertyDescriptors = BeanUtil.getPropertyDescriptors(object.getClass());
        Stream.of(propertyDescriptors).forEach(temp -> {
            String name = temp.getName();
            Object value = null;
            try {
                value = temp.getReadMethod().invoke(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
            dataMap.put(name, value);
        });
    }


}
