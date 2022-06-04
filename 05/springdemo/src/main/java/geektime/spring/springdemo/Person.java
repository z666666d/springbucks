package geektime.spring.springdemo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Person {

    private long id;

    private String name;

    private int score;

}
