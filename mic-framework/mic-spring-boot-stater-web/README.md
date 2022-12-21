# web通用集成

## web集成示例
~~~
server:
  port: 8082

#springboot大于2.4的都需要配置这个
spring:
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848

  application:
    name: mic-biz-web

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