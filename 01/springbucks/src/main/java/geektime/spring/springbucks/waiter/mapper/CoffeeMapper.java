package geektime.spring.springbucks.waiter.mapper;

import geektime.spring.springbucks.waiter.model.Coffee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CoffeeMapper {

    int save(Coffee coffee);

    Coffee selectById(@Param("id") Long id);

    List<Coffee> selectByIds(@Param("ids") List<String> ids);

    List<Coffee> selectAll();

    void deleteById(@Param("id") Long id);

    void updateById(Coffee coffee);
}
