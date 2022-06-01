# 基本命令

1、启动redis

通过redis-server启动redis

```shell
./redis-server ../redis.conf     
#后面为配置文件地址，如果不写则会全部使用默认配置
```

2、redis客户端

redis的src目录下自带一个redis-cli工具。作为客户端连接redis数据库

```shell
redis-cli -h {host} -p {port}
```

```shell
[root@hadoop100 src]# ./redis-cli -h 192.168.17.100 -p 6379
192.168.17.100:6379> 
```

3、auth

如果在配置文件中配置了密码，客户端连接就需要进行权限校验，通过auth命令输入密码

```shell
192.168.17.100:6379> auth 123456
OK
```

不通过密码校验进行其他操作会报错

```shell
192.168.17.100:6379> set 123 111
(error) NOAUTH Authentication required.
```

4、select

redis配置默认有16个数据库，默认使用第0个数据库，可以通过select进行切换

```shell
192.168.17.100:6379> select 3
OK
192.168.17.100:6379[3]> 
```

5、dbsize

dbsize命令用于查看当前数据库键的数量

```shell
192.168.17.100:6379> dbsize
(integer) 1
```

6、keys

keys命令可以查看键列表，参数可用*作为通配符  `keys *`可以查看所有的键

```shell
192.168.17.100:6379> keys *
1) "name"
```

7、flushall/flushdb

清空数据库命令，flushall清除所有数据库，flushdb清空当前库

8、ping

测试连通性，连接正常会返回PONG

```shell
192.168.17.100:6379> ping
PONG
```

9、swapdb index index

交换两个数据库。交换后数据被互换，连接不用重新建立。

10、quit

关闭连接，退出

# 五大数据类型

## 1 key

Redis存储数据为key-value形式的。关于key的一些常用操作：

| 命令                             | 说明                                                       |
| -------------------------------- | ---------------------------------------------------------- |
| set key value                    | 设置key的值为value，如果已存在则修改，如果不存在则新建     |
| get key                          | 获取key的值，没有则返回null                                |
| getset key value                 | 将key设置为value并返回旧值                                 |
| mget key [key ...]               | 获取一个或多个key的值，多个key之间用空格分开               |
| setex key seconds value          | 设置key的值为value，并在n秒后过期                          |
| setnx key value                  | 仅在key不存在时设置                                        |
| getrange key start end           | 获取key值的字符串子串                                      |
| getbit key offset                | 对 key 所储存的字符串值，获取指定偏移量上的位(bit)。       |
| setbit key offset value          | 对 key 所储存的字符串值，设置或清除指定偏移量上的位(bit)。 |
| setrange key offset value        | 用value覆盖给定key的字符串值，偏移量为offset               |
| strlen key                       | 返回key储存字符串的长度                                    |
| mset key value [key value ...]   | 同时设置多对key value的值                                  |
| msetnx key value [key value ...] | 同时设置多对key value的值，仅当key不存在时设置             |
| psetex key milliseconds value    | 设置key的值为value，并在n毫秒后过期                        |
| incr key                         | key存储的数值加一                                          |
| incrby key increment             | key存储的数值增加指定值                                    |
| incrbyfloat key increment        | key存储的数值增加指定值（浮点数）                          |
| decr key                         | key存储的值减一                                            |
| decrby key increment             | key存储的值减指定值                                        |
| append key value                 | 如果key存在，将value追加到值末尾                           |
| del key                          | 删除指定key                                                |
| dump key                         | 导出指定key，值的编码格式和 RDB 文件保持一致。             |
| exists key                       | key是否存在                                                |
| expire key seconds               | 设置key值n秒后过期                                         |
| keys pattern                     | 查找所有匹配的key                                          |
| move key db                      | 将key移动到指定数据库                                      |
| pexpire key milliseconds         | 指定key值n毫秒后过期                                       |
| persist key                      | 移除key的过期时间                                          |
| pttl key                         | 获取key的过期时间，毫秒                                    |
| ttl key                          | 获取key的过期时间，秒                                      |
| randomkey                        | 返回一个随机的key                                          |
| rename key newkey                | 将key重命名                                                |
| renamenx key newkey              | newkey不存在时，将key重命名为newkey                        |
| type key                         | 返回key存储值得数据类型                                    |

## 2 String

String类型是redis最基本的数据类型，一个key对应一个value

String类型是二进制安全的，可以包含任何数据，比如图片或者序列化对象。一个redis的字符串value最大可以是512M

String类型的存储结构类似于Java的ArrayList，动态字符串，采用预分配冗余空间的方式来减少内存的频繁分配。1M以内每次扩容加倍，1M以后每次扩容1M。

String操作：

| 命令                      | 说明                                        |
| ------------------------- | ------------------------------------------- |
| append key value          | 向key中追加字符串，如果key不存在就相当于set |
| strlen key                | 获得字符串长度                              |
| incr key                  | 自增1，原子操作                             |
| decr key                  | 自减1，原子操作                             |
| incrby key increment      | 增加n，原子操作                             |
| decrby key decrement      | 减去n，原子操作                             |
| getrange key start end    | 截取字符串                                  |
| setrange key offset value | 替换指定位置开始的字符串                    |

String类型数据操作的**注意事项**：

- 数据操作不成功的反馈与操作正常之间的差异
  - 返回表示结果成功与否（Integer(0)---->失败	Integer(1)------>成功）
  - 返回运行结果值（Integer(3)---->3个	Integer(1)------>1个）
- 数据未查询到
  - （nil）等同于null
- 数据的最大存储量
  - 512MB
- 数值计算的最大范围（long类型的最大值）
  - 2的31次方    9223372036854774807

String类型的应用场景：

- 主页高频访问信息显示控制，例如访问数、点赞数等等
  - 解决方案1：redis中这些用户信息，以用户主键和属性值作为key，后台设定定时刷新策略即可
    - user:ID:123456789:fans    123456   粉丝数
    - user:ID:123456789:blogs   2381       微博数
    - user:ID:123456789:focuss  938        关注数
  - 解决方案2：以json格式存储用户数据，也可以用hash
    - user:ID:123456789       {ID:123456789,name:张三,fans:123456}
- 应用于各种结构性和非结构性高热度数据访问加速
  - 数据库热点数据key命名规范
    - 表名:主键名:主键值:字段名



## 3 List

Redis的List是简单的字符串列表，按照插入顺序排序。可以添加一个元素到列表的头部或尾部。

他的底层实际上是链表结构。插入和删除操作快，时间复杂度为O(1)，但是索引定位慢，时间复杂度为O(n)。

当列表弹出了最后一个元素之后，该数据结构自动被删除，内存被回收。

Redis 的列表结构常用来做异步队列使用。将需要延后处理的任务结构体序列化成字符
串塞进 Redis 的列表，另一个线程从这个列表中轮询数据进行处理。

| 命令                                  | 说明                                                   |
| ------------------------------------- | ------------------------------------------------------ |
| lpush key value [value ...]           | 从左边入队一个或多个元素                               |
| rpush key value [value ...]           | 从右边入队一个或多个元素                               |
| lrange key start stop                 | 获取指定区间的元素，stop为-1表示取到队尾               |
| lpop key                              | 从左边出队一个元素                                     |
| rpop key                              | 从右边出队一个元素                                     |
| lindex key index                      | 获取索引位置的元素                                     |
| llen key                              | 返回元素数量                                           |
| lrem key count value                  | 删除count个某个元素                                    |
| ltrim key start stop                  | 截取指定位置的元素并赋给key                            |
| rpoplpush source destination          | 从源列表右边取出一个元素从左边入队目标列表             |
| lset key index value                  | 修改指定索引位置的元素                                 |
| linsert key BEFORE\|AFTER pivot value | 在某个元素前或后插入元素                               |
| blpop key [key ...] timeout           | 从左边出队一个元素，没有就阻塞                         |
| brpop key [key ...] timeout           | 从右边出队一个元素，没有就阻塞                         |
| brpoplpush source destination timeout | 从源列表右边取出一个元素从左边入队目标列表，没有就阻塞 |
| lpushx key value                      | 当列表存在时，再从左边入队一个元素                     |

总结：List是一个字符串链表，left、right都可以插入、添加。

如果键不存在，则创建新的链表，如果已存在，则新增内容。

如果值全部移除，那么对应的key也就消失。

由于是链表结构，所以对头尾操作效率都很高，但是要对中间元素进行操作效率就低了。

对于阻塞命令blpop、brpop、brpoplpush对应的key不一定存在。但是如果存在而持有的value不是list类型则会报错。

**注意事项：**

- list存的值都是String类型的，数据库总容量是有限的，最多2的32次方减1
- list有索引的概念，但是操作数据时通常是以队列的形式入队出队操作，或以栈的形式入栈出栈操作
- 获取全部数据操作结束索引设置为-1
- list可以对数据进行分页操作，通常第一页的信息来自list，第二页及更多的数据通过数据库的形式加载

**应用场景：**

- list主要应用于一些多数据且有顺序的场景：如粉丝列表、关注列表、点赞列表等等

**慢操作：**

list类型的一些指令是需要进行遍历操作的，复杂度为O(n)，当list中元素较多的时候慎用。

`lindex key index`

由于底层是链表结构，所以需要对链表进行遍历，性能随着index参数的增大而变差。

`lrange key start end`

获取指定范围内所有的元素，O(n)复杂度，慎用

`ltrim key start end`

截取区间内的元素，索引可以为负数，-1代表倒数第一个，-2代表倒数第二个

**快速列表：**

Redis的list类型底层存储不仅仅是链表结构这么简单，而是称之为`quicklist`快速列表的结构。在元素较少的情况下会使用一块连续的内存存储空间，这个结构是`ziplist`。当数据量较多时，则使用双向指针将多个`ziplist`连接起来，组成`quicklist`。这样既保证了快速的删除、插入性能，又不会出现太多的空间冗余。

## 4 Set

Set集合类型是String类型的无序集合。他是通过HashTable实现的。

| 命令                            | 说明                           |
| ------------------------------- | ------------------------------ |
| sadd key member [member ...]    | 向set集合中添加一个或多个值    |
| smembers key                    | 获取集合中所有的元素           |
| sismember key member            | 查看是不是set集合的元素        |
| scard key                       | 获取set集合的元素个数          |
| srem key member [member ...]    | 删除set中的一个或多个元素      |
| srandmember key [count]         | 从集合中随机获取count个元素    |
| spop key [count]                | 随机删除并获取一个集合中的元素 |
| smove source destination member | 将集合中元素移动到另一个元素中 |
| sdiff key [key ...]             | 获取key与其他集合不同的元素    |
| sinter key [key ...]            | 获取集合的交集                 |
| sunion key [key ...]            | 获取集合的并集                 |

## 5 Hash

redis的Hash是一个键值对的集合，类似Java里的Map<String,Object>。

是一个String类型的field和value的映射表，hash特别适合存储对象。

hash类型，底层使用哈希表结构实现数据存储，如果field数量较少，存储结构优化为类数组结构，如果field数量较多，存储结构使用HashMap结构。数组+链表的形式，rehash时是渐进式的，避免阻塞服务。

hash 也有缺点，hash 结构的存储消耗要高于单个字符串，到底该使用 hash 还是字符
串，需要根据实际情况再三权衡。

| 命令                                    | 说明                           |
| --------------------------------------- | ------------------------------ |
| hset key field value                    | 设置hash的一个字段             |
| hget key field                          | 获取一个字段的值               |
| hmset key field value [field value ...] | 设置多个字段的值               |
| hmget key field [field ...]             | 获取多个字段的值               |
| hgetall key                             | 获取所有的键值对               |
| hdel key field [field ...]              | 删除一个或多个字段             |
| hlen key                                | 获取字段数量                   |
| hexists key field                       | 判断是否包含某个字段           |
| hkeys key                               | 获取所有的字段                 |
| hvals key                               | 获取所有的值                   |
| hincrby key field increment             | 指定字段值增加整数             |
| hincrbyfloat key field increment        | 增加浮点数                     |
| hsetnx key field value                  | 当字段不存在时，增加一个字段   |
| hstrlen key field                       | 获取指定字段对应的值字符串长度 |

Hash数据类型操作**注意事项**：

- hash类型的value只能存储字符串，不允许存储其他数据类型，不存在嵌套现象。如果数据未获取到，返回nil
- 每个hash可存储2的32次方-1个键值对
- hash类型十分贴近对象数据存储形式，并且可以灵活地添加删除属性，但是hash的设计初衷不是存大量对象，不可滥用，更不可将hash作为对象列表使用
- hgetall可以获取全部属性，如果内部field过多，遍历整体数据效率就很低，可能会成为数据访问的瓶颈

Hash与String中存json比较：

​	Hash格式修改起来更方便，而如果只是读取出来用于展示，没有太多修改操作，使用json字符串更合适。

## 6 ZSet

Zset，Sorted set，有序集合。

和set类型一样也是String类型的集合，且不允许数据重复。

不同的是每个元素都会关联一个double类型的分数。redis正是通过分数来为集合中的成员进行从大到小的排序。

zset的成员都是唯一的，但是分数（score）却可以是重复的。它的内部实现用的是一种叫着「跳跃列表」的数据结构。

| 命令                                                         | 说明                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| zadd key [NX\|XX] [CH] [INCR] score member [score member ...] | 添加一个或多个元素，每个元素对应一个score                    |
| zrange key start stop [WITHSCORES]                           | 获取指定范围内的元素，添加withscores会增加分数返回           |
| zrangebyscore key min max [WITHSCORES] [LIMIT offset count]  | 返回指定score范围内的元素，score前加（表示不包含。支持limit分页 |
| zrem key member [member ...]                                 | 删除指定的一个或多个元素                                     |
| zcard key                                                    | 返回元素数量                                                 |
| zcount key min max                                           | 指定分数范围内的元素                                         |
| zrank key member                                             | 返回指定元素的索引                                           |
| zscore key member                                            | 返回指定元素的socre                                          |
| zrevrank key member                                          | 返回指定元素逆序的索引                                       |
| zrevrange key start stop [WITHSCORES]                        | 逆序返回指定范围内的元素                                     |
| zrevrangebyscore key max min [WITHSCORES] [LIMIT offset count] | 逆序返回指定分数范围内的元素                                 |
| zincrby key increment member                                 | 增加指定元素的分数                                           |

**注意事项：**

- score的存储空间是64位。
- score保存的数据可以是double类型，基于浮点数的特征，可能会丢失精度
- zset底层存储还是基于set结构，因此数据不能重复，如果添加相同数据。score会被覆盖。

## 7 通用规则

**1、create if not exists**

如果容器不存在，那就创建一个，再进行操作。

**2 、drop if no elements**

如果容器里元素没有了，那么立即删除元素，释放内存。

**3、过期时间**

过期时间都是针对key设置的，比如hash、list等都是针对真个容器设置的过期时间，不能针对某个元素设置。另外对String类型设置了过期时间后，如果使用set指令修改了，那么这个String的过期时间将会失效。