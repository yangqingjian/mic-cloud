# swagger插件

## 配置示例
~~~
#springboot大于2.4的都需要配置这个
spring:
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
  base-package: cn.mic.cloud.biz.swagger.controller
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
