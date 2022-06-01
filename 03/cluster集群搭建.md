## cluster集群搭建

**1、配置文件**

```shell
cluster-enabled no|yes
#启动cluster配置

cluster-config-file <filename>
#集群配置文件名，尽管有此选项的名称，但这不是用户可编辑的配置文件，而是Redis群集节点每次发生更改时自动保留群集配置的文件
#该文件列出了群集中其他节点，它们的状态，持久变量等等。 由于某些消息的接收，通常会将此文件重写并刷新到磁盘上。

cluster-node-timeout <milliseconds>
#Redis群集节点可以不可用的最长时间，而不会将其视为失败。 如果主节点超过指定的时间不可达，它将由其从属设备进行故障切换。

cluster-require-full-coverage no
#默认为yes，表示只有所有哈希槽有主节点管理的时候，集群才可以接受查询。

cluster-migration-barrier 1
#可以配置值为1。master的slave数量大于该值，slave才能迁移到其他孤立master上，如这个参数若被设为2，那么只有当一个主节点拥有2 个可工作的从节点时，它的一个从节点会尝试迁移。

cluster-replica-validity-factor 10
#集群副本有效因子，此参数设置后，需要在集群可用性和数据的完整性之间进行取舍。

```

**2、集群启动**

- 首先要启动集群中所有的节点
- 执行redis-trib.rb脚本来创建集群，必须有ruby和rubygem环境



创建命令

```txt
./redis-trib.rb create --replicas 1 127.0.0.1:6379 127.0.0.1:6380 127.0.0.1:6381 127.0.0.1:6382 127.0.0.1:6383 127.0.0.1:6384
```

1：表示一个master对应1个slave    即配置6个就前三个为master，后三个为slave



读写数据时，需要通过`./redis-cli -c`命令启动客户端，进行集群操作。否则可能会返回对应的slot不在本节点。



**3、节点操作指令**

- cluster nodes
  - 查看集群节点信息
- cluster replicate master-id
  - 进入一个从节点，切换其主节点
- cluster meet ip:port
  - 新增一个主节点
- cluster forget id
  - 忽略一个已经没有的从节点
- cluster failover
  - 手动故障转移