# service 信息

## 配置示例
~~~
server:
  port: 8081

#mybatis配置
mybatis-plus:
  mapper-locations: classpath:mybatis/mapper/*Mapper.xml
  typeAliasesPackage: com.ihome.caas.domain
  global-config:
    #主键类型  0:"数据库ID自增", 1:"用户输入ID",2:"全局唯一ID (数字类型唯一ID)", 3:"全局唯一ID UUID";
    id-type: 2
    #字段策略 0:"忽略判断",1:"非 NULL 判断"),2:"非空判断"
    field-strategy: 1
    #驼峰下划线转换
    db-column-underline: true
    #刷新mapper 调试神器
    refresh-mapper: true
    #数据库大写下划线转换
    #capital-mode: true
    #逻辑删除配置
    logic-delete-value: 1
    logic-not-delete-value: 0
    #必须主动设置数据库类型为mysql,并且设置关键字段的替换
    db-type: mysql
    #必须设置好格式化字符
    db-config:
      columnFormat: "`%s`"
      selectStrategy: not_empty
  configuration:
    map-underscore-to-camel-case: true
    cache-enabled: false
    #必须指明jdbcTypeForNull的值，否则在执行insertAllColumn和updateAllColumn会报错
    jdbcTypeForNull: "NULL"
#    default-enum-type-handler: com.baomidou.mybatisplus.extension.handlers.MybatisEnumTypeHandler

#springboot大于2.4的都需要配置这个
spring:
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848

  application:
    name: mic-biz-test

  datasource:
    url: jdbc:mysql://172.16.0.205:3306@/dev-ihome-caas?useUnicode=true&serverTimezone=CTT&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false&useSSL=false&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true
    username: root
    password: SHJ-dev-test
    driver-class-name: com.mysql.cj.jdbc.Driver
    tomcat:
      max-active: 10
      validation-interval: 30000
      min-idle: 2
      initial-size: 2
      max-idle: 5
      min-evictable-idle-time-millis: 6000
  ##redis哨兵模式配置
  redis:
    mode: sentinel
    password: abcdABCDshjSHJ
    lettuce:
      pool:
        #最大连接数
        max-active: 20
        #最大阻塞等待时间(负数表示没限制)
        max-wait: 5000
        #最大空闲
        max-idle: 10
        #最小空闲
        min-idle: 5
    sentinel:
      master: mymaster
      nodes: 172.16.0.35:26377,172.16.0.35:26378,172.16.0.35:26379
    redisson:
      master-name: mymaster
      sentinel-addresses: redis://172.16.0.35:26377,redis://172.16.0.35:26378,redis://172.16.0.35:26379
      password: abcdABCDshjSHJ
  mvc:
    pathmatch:
      # 配置策略
      matching-strategy: ant-path-matcher

#knife4j-swagger配置
knife4j:
  # 开启增强配置
  enable: true
  basic:
    enable: true
    # Basic认证用户名
    username: test
    # Basic认证密码
    password: 123

#swagger2参数配置
mic-swagger:
  groupName: 测试
  #基础controller包路径
  base-package: cn.mic.cloud
  #停用
  enable: true
  #接口文档名称
  tittle: 测试swagger
  #描述
  description: 测试swagger
  #版本号
  version: 1.0
  #接口文档联系人
  contactName: yqj
  contactEmail: yqj@qq.com
  contactUrl: gateway.shj.cn
~~~