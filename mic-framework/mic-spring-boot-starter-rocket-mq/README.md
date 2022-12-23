# rocketMq 消息封装

## 配置
#rocketmq
rocketmq:
  #2.54
  name-server: 172.16.0.35:9876
  producer:
    sendMessageTimeout: 10000
    group: mic-biz-test-web
    topic: mic-biz-test-web-topic