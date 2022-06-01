package geektime.spring.springbucks.waiter.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderState {
    INIT("1","下单"),
    PAID("2","已支付"),
    BREWING("3","制作中"),
    BREWED("4","制作完成"),
    TAKEN("5","已取货"),
    CANCELLED("6","订单取消");

    private String value;

    @JsonValue
    private String name;

    OrderState(String value,String name){
        this.value = value;
        this.name = name;
    }

    public String getValue(){
        return this.value;
    }

    public String getName(){
        return this.name;
    }
}
