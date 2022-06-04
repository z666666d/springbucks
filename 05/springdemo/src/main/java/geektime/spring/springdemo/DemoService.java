package geektime.spring.springdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class DemoService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 使用redis的zset实现排序
     */
    public void rankList(){

        // 准备测试数据
        List<Person> personList = new ArrayList<>();
        Person person1 = new Person();
        person1.setId(1);
        person1.setName("张三");
        person1.setScore(77);
        personList.add(person1);

        Person person2 = new Person();
        person2.setId(2);
        person2.setName("李四");
        person2.setScore(82);
        personList.add(person2);

        Person person3 = new Person();
        person3.setId(3);
        person3.setName("王五");
        person3.setScore(60);
        personList.add(person3);

        // 将数据存入redis
        personList.forEach( e -> {
            redisTemplate.opsForZSet().add("scoreList",e.getName(),e.getScore());
        });


        // 获取李四的分数
        double lisiScore = redisTemplate.opsForZSet().score("scoreList","李四");
        log.info("李四的分数为：{}",lisiScore);

        // 增加数据
        redisTemplate.opsForZSet().add("scoreList","赵六",100);
        redisTemplate.opsForZSet().add("scoreList","孙七",69);
        redisTemplate.opsForZSet().add("scoreList","周八",84);

        // 查询前五名
        Set<ZSetOperations.TypedTuple<String>> topFive = redisTemplate.opsForZSet().reverseRangeWithScores("scoreList",0,4);
        topFive.forEach(e -> {
            log.info(e.getValue()+":"+e.getScore());
        });



        // 更新李四分数
        redisTemplate.opsForZSet().add("scoreList","李四",99);

        // 查询前五名
        Set<ZSetOperations.TypedTuple<String>> topFive2 = redisTemplate.opsForZSet().reverseRangeWithScores("scoreList",0,4);
        topFive2.forEach(e -> {
            log.info(e.getValue()+":"+e.getScore());
        });

        // 测试结束删除数据
        redisTemplate.delete("scoreList");
    }

    /**
     * 基于redis实现全局自增ID
     */
    public void generateId(){
        Person person1 = new Person();
        person1.setName("张三");
        person1.setScore(77);
        person1.setId(redisTemplate.opsForValue().increment("id"));


        Person person2 = new Person();
        person2.setId(redisTemplate.opsForValue().increment("id"));
        person2.setName("李四");
        person2.setScore(82);

        Person person3 = new Person();
        person3.setId(redisTemplate.opsForValue().increment("id"));
        person3.setName("王五");
        person3.setScore(60);

        log.info("张三：{},李四：{},王五：{}",person1.getId(),person2.getId(),person3.getId());

        // 测试结束删除数据
        redisTemplate.delete("id");
    }


    /**
     * 基于bitmap实现id去重
     */
    public void removeDuplicate(){

        List<Integer> ids = Arrays.asList(1,3,5,2,4,15,4,66,13,66,77);

        // 保存去重后的id
        List<Integer> newIds = new ArrayList<>();

        ids.forEach(e -> {
            boolean flag = redisTemplate.opsForValue().getBit("idBit",e);
            if(!flag){
                // 不存在，将id存入新的List，并设置对应的offset为1
                redisTemplate.opsForValue().setBit("idBit",e,true);
                newIds.add(e);
            }
        });

        log.info("id去重后：{}",newIds);

        // 测试结束删除数据
        redisTemplate.delete("idBit");
    }

    /**
     * 基于HyperLogLog记录点击量
     */
    public void clickRate(){

        // 模拟点击
        for(int i = 0;i < 10000;i++){
            redisTemplate.opsForHyperLogLog().add("clickcount",i);
        }

        // 统计数量
        log.info("点击量：{}",redisTemplate.opsForHyperLogLog().size("clickcount"));

        // 测试结束删除数据
        redisTemplate.delete("clickcount");
    }
}
