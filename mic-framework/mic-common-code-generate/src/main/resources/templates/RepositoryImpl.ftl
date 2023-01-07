package ${repositoryImplPackage};

import ${domainPackage}.${tableNameFirstUpper};
import ${mapperPackage}.${tableNameFirstUpper}Mapper;
import cn.mic.cloud.mybatis.plus.core.BaseEntityRepositoryImpl;
import org.springframework.stereotype.Repository;
import ${repositoryPackage}.${tableNameFirstUpper}Repository;

/**
* ${tableComment}  repository接口实现类
*
* @author : ${author}
* @date : ${date}
*/
@Repository
public class ${tableNameFirstUpper}RepositoryImpl extends BaseEntityRepositoryImpl<${tableNameFirstUpper}Mapper,${tableNameFirstUpper}> implements ${tableNameFirstUpper}Repository {


}
