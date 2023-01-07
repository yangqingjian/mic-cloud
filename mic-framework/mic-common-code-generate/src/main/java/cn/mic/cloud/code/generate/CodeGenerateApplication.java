package cn.mic.cloud.code.generate;

import cn.mic.cloud.code.generate.config.CodeGenerateConfig;
import cn.mic.cloud.code.generate.core.CodeGenerateCore;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Stream;

/**
 * @author : YangQingJian
 * @date : 2023/1/4
 */
@Slf4j
public class CodeGenerateApplication {

    public static void main(String[] args) {
        /**
         * 1 确定好模块的名称 例子：“demo”
         * 2 确定要生成的文件路径，建议不要直接生成到项目里面去了 例子：“D:\\code-generate”
         * 3 确定要子包的路径,注意加.号的位置，为空时，直接为空字符串,例子：“.test”
         */
        CodeGenerateConfig config = new CodeGenerateConfig("YangQingJian", "caas", "E:\\code\\gitee\\mic-cloud\\mic-biz-caas", ".basic");
        CodeGenerateCore codeGenerateCore = new CodeGenerateCore(config);
        String[] tableNames = "caas_region".split(",");
        Stream.of(tableNames).forEach(temp -> codeGenerateCore.startGenerate(temp.trim()));
    }

}
