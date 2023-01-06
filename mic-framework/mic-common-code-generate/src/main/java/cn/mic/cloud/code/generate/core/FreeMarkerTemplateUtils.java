package cn.mic.cloud.code.generate.core;

import freemarker.cache.NullCacheStorage;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.IOException;

/**
 * @author : YangQingJian
 * @date : 2023/1/4
 */
public class FreeMarkerTemplateUtils {

    private FreeMarkerTemplateUtils() {
    }

    private static final Configuration CONFIGURATION = new Configuration(Configuration.VERSION_2_3_22);

    static {
        //这里比较重要，用来指定加载模板所在的路径
        try {
            String path = FreeMarkerTemplateUtils.class.getResource("/").toString().replaceAll("file:/", "");
            CONFIGURATION.setDirectoryForTemplateLoading(new File(path + "/templates"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        CONFIGURATION.setDefaultEncoding("UTF-8");
        CONFIGURATION.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        CONFIGURATION.setCacheStorage(NullCacheStorage.INSTANCE);
    }

    public static Template getTemplate(String templateName) throws IOException {
        try {
            return CONFIGURATION.getTemplate(templateName);
        } catch (IOException e) {
            throw e;
        }
    }

    public static void clearCache() {
        CONFIGURATION.clearTemplateCache();
    }
}
