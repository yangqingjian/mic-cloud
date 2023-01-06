package ${webPackage};

import ${domainPackage}.${tableNameFirstUpper};
import cn.mic.cloud.common.web.core.AbstractBaseEntityController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* ${tableComment} web接口
*
* @author : ${author}
* @date : ${date}
*/
@RestController
@RequestMapping("/${tableNameCame}")
@Api(tags = "${tableComment}")
@Slf4j
public class ${tableNameFirstUpper}Controller extends AbstractBaseEntityController<${tableNameFirstUpper}> {


}
