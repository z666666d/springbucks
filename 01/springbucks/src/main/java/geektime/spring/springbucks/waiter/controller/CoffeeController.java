package geektime.spring.springbucks.waiter.controller;

import com.github.pagehelper.PageInfo;
import geektime.spring.springbucks.waiter.controller.request.NewCoffeeRequest;
import geektime.spring.springbucks.waiter.controller.request.UpdateCoffeeRequest;
import geektime.spring.springbucks.waiter.model.Coffee;
import geektime.spring.springbucks.waiter.service.CoffeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/coffee")
@Slf4j
public class CoffeeController {
    @Autowired
    private CoffeeService coffeeService;

    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public String addCoffee(@Valid @RequestBody NewCoffeeRequest newCoffee) {
        coffeeService.saveCoffee(newCoffee.getName(), newCoffee.getPrice());
        return "添加咖啡成功！";
    }

    @GetMapping(path = "/", params = "!name")
    public List<Coffee> getAll() {
        return coffeeService.getAllCoffee();
    }

    @GetMapping("/{id}")
    public Coffee getById(@PathVariable Long id) {
        Coffee coffee = coffeeService.getCoffeById(id);
        log.info("Coffee {}:", coffee);
        return coffee;
    }

    @GetMapping("/list/{ids}")
    public List<Coffee> getByIds(@PathVariable String ids) {
        return coffeeService.getCoffeByIds(ids);
    }

    @GetMapping("/{pageNum}/{pageSize}")
    public PageInfo getByPage(@PathVariable Integer pageNum,@PathVariable Integer pageSize) {
        return coffeeService.getCoffeeByPage(pageNum,pageSize);
    }

    @PutMapping(path = "/", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String updateCoffee(@Valid @RequestBody UpdateCoffeeRequest updateCoffee) {
        coffeeService.updCoffeById(updateCoffee.getId(),updateCoffee.getName(),updateCoffee.getPrice());
        return "修改咖啡成功";
    }


    @DeleteMapping(path = "/{id}")
    public String delCoffee(@PathVariable Long id) {
        coffeeService.delCoffeById(id);
        return "删除咖啡成功";
    }

    @DeleteMapping(path = "/cache")
    public String delCoffeeCache() {
        coffeeService.reloadCoffee();
        return "清理缓存成功";
    }
}
