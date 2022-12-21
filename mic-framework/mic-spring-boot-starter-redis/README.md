# redis封装

## redission封装
~~~
  ##redis哨兵模式配置
  redis:
    mode: sentinel
    password: xxx
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
      nodes: xxx,xxx,xxx
    redisson:
      master-name: mymaster
      sentinel-addresses: xxx,xxx,xxx
      password: xxx
~~~
## redis锁封装


