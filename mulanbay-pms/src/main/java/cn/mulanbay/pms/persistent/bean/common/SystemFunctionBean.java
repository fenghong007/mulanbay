package cn.mulanbay.pms.persistent.bean.common;

import java.math.BigInteger;

/**
 * Created by fenghong on 2017/9/9.
 */
public class SystemFunctionBean {

    private BigInteger id;

    private BigInteger pid;

    private String name;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public BigInteger getPid() {
        return pid;
    }

    public void setPid(BigInteger pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
