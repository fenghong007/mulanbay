package cn.mulanbay.pms.web.bean.request;

import cn.mulanbay.common.aop.BindUser;

/**
 * Created by fenghong on 2017/8/31.
 * 通用的树查询
 */
public class CommonTreeSearch implements BindUser {

    private Boolean needRoot;

    private Long userId;

    public Boolean getNeedRoot() {
        return needRoot;
    }

    public void setNeedRoot(Boolean needRoot) {
        this.needRoot = needRoot;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
