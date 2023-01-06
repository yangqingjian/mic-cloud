package ${servicePackage};

import ${apiPackage}.${tableNameFirstUpper}Api;
import ${domainPackage}.${tableNameFirstUpper};
import cn.mic.cloud.mybatis.plus.core.BaseEntityService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;

/**
 * ${tableComment} service接口
 *
 * @author : ${author}
 * @date : ${date}
 */
@RestController
@RequestMapping("/${tableNameCame}")
@Api(tags = "${tableComment}")
public interface ${tableNameFirstUpper}Service extends BaseEntityService<${tableNameFirstUpper}> , ${tableNameFirstUpper}Api {



}
