package ${feignPackage};

import ${apiPackage}.${tableNameFirstUpper}Api;
import ${domainPackage}.${tableNameFirstUpper};
import cn.mic.cloud.freamework.common.core.BaseEntityFeign;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * ${tableComment} 接口抽象层类
 *
 * @author : ${author}
 * @date : ${date}
 */
@FeignClient(value = "${projectPreName}-service", path = "/${tableNameCame}" , contextId = "${tableNameCame}Feign")
public interface ${tableNameFirstUpper}Feign extends ${tableNameFirstUpper}Api, BaseEntityFeign<${tableNameFirstUpper}> {

}