import geektime.spring.springdemo.DemoService;
import geektime.spring.springdemo.SpringDemoApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringDemoApplication.class)
@Slf4j
public class SpringDemoApplicationTests {

	@Autowired
	private DemoService demoService;

	@Test
	public void test() {
		log.info("测试排名开始");
		demoService.rankList();
		log.info("测试排名结束");

		log.info("测试全局ID开始");
		demoService.generateId();
		log.info("测试全局ID结束");

		log.info("测试去重开始");
		demoService.removeDuplicate();
		log.info("测试去重结束");

		log.info("测试点击量开始");
		demoService.clickRate();
		log.info("测试点击量结束");
	}

}
