package cn.mulanbay.schedule.enums;

/**
 * ${DESCRIPTION}
 *
 * @author fenghong
 * @create 2017-10-19 13:56
 **/
public enum JobExecuteResult {
    SUCESS(0,"成功"),FAIL(1,"失败"),SKIP(2,"跳过"),DUPLICATE(3,"重复");

    private Integer value;

    private String name;

    JobExecuteResult(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
