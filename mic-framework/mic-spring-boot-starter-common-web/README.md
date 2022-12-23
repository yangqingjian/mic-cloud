# web通用的集成

## seata 集成
### 1. 引入依赖
~~~
<dependency>
    <groupId>io.seata</groupId>
    <artifactId>seata-spring-boot-starter</artifactId>
</dependency>
<!-- spring-cloud项目必须引入 -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
</dependency>
~~~
### 2. seata-服务端配置，registry.conf
~~~
registry {
  # file 、nacos 、eureka、redis、zk、consul、etcd3、sofa
  type = "nacos"
  loadBalance = "RandomLoadBalance"
  loadBalanceVirtualNodes = 10
  nacos {
    application = "local-seata-server"
    serverAddr = "192.168.71.122:8848"
    group = "local"
    namespace = "local"
    cluster = "default"
    username = "nacos"
    password = "nacos"
  }
}
config {
  # file、nacos 、apollo、zk、consul、etcd3
  type = "nacos"
  nacos {
    serverAddr = "192.168.71.122:8848"
    namespace = "local"
    group = "local"
    username = "nacos"
    password = "nacos"
  }
}
~~~
### 3. nacos的注册中心配置
#### 3.1 seata-config.txt 放在nacos的bin目录下即可
~~~
transport.type=TCP
transport.server=NIO
transport.heartbeat=true
transport.enableClientBatchSendRequest=true
transport.threadFactory.bossThreadPrefix=NettyBoss
transport.threadFactory.workerThreadPrefix=NettyServerNIOWorker
transport.threadFactory.serverExecutorThreadPrefix=NettyServerBizHandler
transport.threadFactory.shareBossWorker=false
transport.threadFactory.clientSelectorThreadPrefix=NettyClientSelector
transport.threadFactory.clientSelectorThreadSize=1
transport.threadFactory.clientWorkerThreadPrefix=NettyClientWorkerThread
transport.threadFactory.bossThreadSize=1
transport.threadFactory.workerThreadSize=default
transport.shutdown.wait=3
service.vgroupMapping.my_test_tx_group=default
service.default.grouplist=127.0.0.1:8091
service.enableDegrade=false
service.disableGlobalTransaction=false
client.rm.asyncCommitBufferLimit=10000
client.rm.lock.retryInterval=10
client.rm.lock.retryTimes=30
client.rm.lock.retryPolicyBranchRollbackOnConflict=true
client.rm.reportRetryCount=5
client.rm.tableMetaCheckEnable=falsestore.session.mode
client.rm.tableMetaCheckerInterval=60000
client.rm.sqlParserType=druid
client.rm.reportSuccessEnable=false
client.rm.sagaBranchRegisterEnable=false
client.rm.tccActionInterceptorOrder=-2147482648
client.tm.commitRetryCount=5
client.tm.rollbackRetryCount=5
client.tm.defaultGlobalTransactionTimeout=60000
client.tm.degradeCheck=false
client.tm.degradeCheckAllowTimes=10
client.tm.degradeCheckPeriod=2000
client.tm.interceptorOrder=-2147482648
store.mode=db
store.lock.mode=file
store.session.mode=file
store.publicKey=
store.file.dir=file_store/data
store.file.maxBranchSessionSize=16384
store.file.maxGlobalSessionSize=512
store.file.fileWriteBufferCacheSize=16384
store.file.flushDiskMode=async
store.file.sessionReloadReadSize=100
store.db.datasource=druid
store.db.dbType=mysql
store.db.driverClassName=com.mysql.jdbc.Driver
store.db.url=jdbc:mysql://127.0.0.1:3306/biz-local-seata?useUnicode=true&rewriteBatchedStatements=true
store.db.user=root
store.db.password=123456
store.db.minConn=5
store.db.maxConn=30
store.db.globalTable=global_table
store.db.branchTable=branch_table
store.db.queryLimit=100
store.db.lockTable=lock_table
store.db.maxWait=5000
server.recovery.committingRetryPeriod=1000
server.recovery.asynCommittingRetryPeriod=1000
server.recovery.rollbackingRetryPeriod=1000
server.recovery.timeoutRetryPeriod=1000
server.maxCommitRetryTimeout=-1
server.maxRollbackRetryTimeout=-1
server.rollbackRetryTimeoutUnlockEnable=false
server.distributedLockExpireTime=10000
client.undo.dataValidation=true
client.undo.logSerialization=jackson
client.undo.onlyCareUpdateColumns=true
server.undo.logSaveDays=7
server.undo.logDeletePeriod=86400000
client.undo.logTable=undo_log
client.undo.compress.enable=true
client.undo.compress.type=zip
client.undo.compress.threshold=64k
log.exceptionRate=100
transport.serialization=seata
transport.compressor=none
metrics.enabled=false
metrics.registryType=compact
metrics.exporterList=prometheus
metrics.exporterPrometheusPort=9898
~~~
#### 3.2 nacos.sh脚本 放在bin目录下即可
~~~
#!/bin/sh
# Copyright 1999-2019 Seata.io Group.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at、
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

while getopts ":h:p:g:t:u:w:" opt
do
  case $opt in
  h)
    host=$OPTARG
    ;;
  p)
    port=$OPTARG
    ;;
  g)
    group=$OPTARG
    ;;
  t)
    tenant=$OPTARG
    ;;
  u)
    username=$OPTARG
    ;;
  w)
    password=$OPTARG
    ;;
  ?)
    echo " USAGE OPTION: $0 [-h host] [-p port] [-g group] [-t tenant] [-u username] [-w password] "
    exit 1
    ;;
  esac
done

if [ -z ${host} ]; then
    host=localhost
fi
if [ -z ${port} ]; then
    port=8848
fi
if [ -z ${group} ]; then
    group="SEATA_GROUP"
fi
if [ -z ${tenant} ]; then
    tenant=""
fi
if [ -z ${username} ]; then
    username=""
fi
if [ -z ${password} ]; then
    password=""
fi

nacosAddr=$host:$port
contentType="content-type:application/json;charset=UTF-8"

echo "set nacosAddr=$nacosAddr"
echo "set group=$group"

urlencode() {
  length="${#1}"
  i=0
  while [ $length -gt $i ]; do
    char="${1:$i:1}"
    case $char in
    [a-zA-Z0-9.~_-]) printf $char ;;
    *) printf '%%%02X' "'$char" ;;
    esac
    i=`expr $i + 1`
  done
}

failCount=0
tempLog=$(mktemp -u)
function addConfig() {
  dataId=`urlencode $1`
  content=`urlencode $2`
  curl -X POST -H "${contentType}" "http://$nacosAddr/nacos/v1/cs/configs?dataId=$dataId&group=$group&content=$content&tenant=$tenant&username=$username&password=$password" >"${tempLog}" 2>/dev/null
  if [ -z $(cat "${tempLog}") ]; then
    echo " Please check the cluster status. "
    exit 1
  fi
  if [ "$(cat "${tempLog}")" == "true" ]; then
    echo "Set $1=$2 successfully "
  else
    echo "Set $1=$2 failure "
    failCount=`expr $failCount + 1`
  fi
}

count=0
for line in $(cat $(dirname "$PWD")/bin/seata-config.txt | sed s/[[:space:]]//g); do
    count=`expr $count + 1`
key=${line%%=*}
    value=${line#*=}
addConfig "${key}" "${value}"
done

echo "========================================================================="
echo " Complete initialization parameters,  total-count:$count ,  failure-count:$failCount "
echo "========================================================================="

if [ ${failCount} -eq 0 ]; then
echo " Init nacos config finished, please start seata-server. "
else
echo " init nacos config fail. "
fi
~~~
#### 3.3 执行【Set store.publicKey= failure】这个不管，因为为空
~~~
cd /data/nacos/bin
sh nacos-config.sh -h 192.168.71.122 -u nacos -w "nacos" -p 8848 -g local -t local
~~~

#### 3.4 seata数据库初始化biz-local-seata
~~~
-- ----------------------------
-- Table structure for branch_table
-- ----------------------------
CREATE TABLE `branch_table` (
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(256) NOT NULL,
  `transaction_id` bigint(20) DEFAULT NULL,
  `resource_group_id` varchar(256) DEFAULT NULL,
  `resource_id` varchar(256) DEFAULT NULL,
  `branch_type` varchar(8) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `client_id` varchar(64) DEFAULT NULL,
  `application_data` varchar(2000) DEFAULT NULL,
  `gmt_create` datetime(6) DEFAULT NULL,
  `gmt_modified` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`branch_id`) USING BTREE,
  KEY `idx_xid` (`xid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for global_table
-- ----------------------------
CREATE TABLE `global_table` (
  `xid` varchar(256) NOT NULL,
  `transaction_id` bigint(20) DEFAULT NULL,
  `status` tinyint(4) NOT NULL,
  `application_id` varchar(256) DEFAULT NULL,
  `transaction_service_group` varchar(256) DEFAULT NULL,
  `transaction_name` varchar(256) DEFAULT NULL,
  `timeout` int(11) DEFAULT NULL,
  `begin_time` bigint(20) DEFAULT NULL,
  `application_data` varchar(2000) DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`xid`) USING BTREE,
  KEY `idx_gmt_modified_status` (`gmt_modified`,`status`) USING BTREE,
  KEY `idx_transaction_id` (`transaction_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for lock_table
-- ----------------------------
CREATE TABLE `lock_table` (
  `row_key` varchar(256) NOT NULL,
  `xid` varchar(256) DEFAULT NULL,
  `transaction_id` bigint(20) DEFAULT NULL,
  `branch_id` bigint(20) NOT NULL,
  `resource_id` varchar(256) DEFAULT NULL,
  `table_name` varchar(256) DEFAULT NULL,
  `pk` varchar(256) DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`row_key`) USING BTREE,
  KEY `idx_branch_id` (`branch_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
~~~

#### 3.5 启动seata
Linux ： sh seata-server.sh -p 8091 -h 127.0.0.1 -m file
windows ： .\seata-server.bat -p 8091 -h 127.0.0.1 -m db
docker 启动【注意配置文件的目录在/data/seata/config】
~~~
docker run -itd --name seata-server  \
        --restart=always \
        -p 8091:8091 \
        -e SEATA_CONFIG_NAME=file:/root/seata-config/registry  \
        -v /data/seata/config:/root/seata-config  \
        seataio/seata-server:1.4.0 \
        --hostname 192.168.1.61
~~~

### 4 集成service业务
#### 4.1 配置
~~~
#seata config
seata:
  tx-service-group: my_test_tx_group
  registry:
    type: nacos
    nacos:
      server-addr: 127.0.0.1:8848
      username: nacos
      password: nacos
      cluster: default
      group: local
      namespace: local
      application: local-seata-server
  config:
    type: nacos
    nacos:
      server-addr: 127.0.0.1:8848
      username: nacos
      password: nacos
      cluster: default
      group: local
      namespace: local
  application-id: ${spring.application.name}-${spring.profiles.active} # Seata 应用名称，默认使用 ${spring.application.name}
~~~

#### 4.2 业务数据库增加undo_log
~~~
CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
~~~




