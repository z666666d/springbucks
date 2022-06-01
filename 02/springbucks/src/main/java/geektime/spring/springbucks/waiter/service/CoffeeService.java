package geektime.spring.springbucks.waiter.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import geektime.spring.springbucks.waiter.mapper.CoffeeMapper;
import geektime.spring.springbucks.waiter.model.Coffee;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@CacheConfig(cacheNames = "CoffeeCache")
public class CoffeeService {

    @Autowired
    private CoffeeMapper coffeeMapper;

    @Transactional
    public void saveCoffee(String name, Money price) {
        coffeeMapper.save(Coffee.builder().name(name).price(price).build());
    }

    @Cacheable
    public List<Coffee> getAllCoffee() {

        return coffeeMapper.selectAll();
    }

    @CacheEvict
    public void reloadCoffee() {
        log.info("清理緩存");
    }

    public PageInfo getCoffeeByPage(int pageNum,int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Coffee> coffeeList = coffeeMapper.selectAll();
        PageInfo<Coffee> pageInfo = PageInfo.of(coffeeList);
        return pageInfo;
    }

    public Coffee getCoffeById(Long id) {
        return coffeeMapper.selectById(id);
    }

    public List<Coffee> getCoffeByIds(String ids) {
        List<String> list = Arrays.asList(ids.split(","));
        return coffeeMapper.selectByIds(list);
    }

    @Transactional
    public void delCoffeById(Long id) {
        coffeeMapper.deleteById(id);
    }

    @Transactional
    public void updCoffeById(Long id,String name, Money price) {
        coffeeMapper.updateById(Coffee.builder().id(id).name(name).price(price).build());
    }
}
