package cn.mic.cloud.biz.caas.domain.constants;

/**
 * @author : YangQingJian
 * @date : 2023/1/6
 */
public interface CaasConstants {
    /**
     * 行政区域的地址
     */
    String REGION_BASE_URL = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2022";

    /**
     * 顶级的url
     */
    String REGION_TOP_URL = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2022/index.html";

    /**
     * 行政区域的顶级编码
     */
    String REGION_TOP_PARENT_CODE = "0";

    /**
     * 第一层级最小值
     */
    int REGION_FIRST_ROW_SIZE = 4;
    /**
     * 第二层级最小值
     */
    int REGION_SECOND_ROW_SIZE = 2;
    /**
     * 第三层级最小值
     */
    int REGION_THIRD_ROW_SIZE = 2;
    /**
     * 第三层级最小值
     */
    int REGION_FOUR_ROW_SIZE = 2;


}
