package cn.mulanbay.pms.persistent.enums;

public enum  MonitorBussType {

    ALL(0,"全部"),
    SCHEDULE(1,"调度"),
    ERROR_CODE(2,"错误代码"),
    SECURITY(3,"安全"),
    SYSTEM(4,"系统");

    private int value;

    private String name;

    MonitorBussType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
