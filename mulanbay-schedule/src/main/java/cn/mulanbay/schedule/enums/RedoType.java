package cn.mulanbay.schedule.enums;

/**
 * ${DESCRIPTION}
 *
 * @author fenghong
 * @create 2017-10-19 13:51
 **/
public enum RedoType {

    CANNOT(0,"不能重做"),MUNUAL_REDO(1,"手动重做"),AUTO_REDO(2,"自动重做"),ALL_REDO(3,"手动、自动重做");

    private Integer value;

    private String name;

    RedoType(Integer value, String name) {
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
