package cn.mulanbay.pms.web.bean.request.common;

import cn.mulanbay.common.aop.BindUser;

/**
 * ${DESCRIPTION}
 *
 * @author fenghong
 * @create 2018-02-17 15:01
 */
public class GetYearTreeSearch implements BindUser{

    private Boolean needRoot;

    private Long userId;

    public Boolean getNeedRoot() {
        return needRoot;
    }

    public void setNeedRoot(Boolean needRoot) {
        this.needRoot = needRoot;
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
