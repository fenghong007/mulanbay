package cn.mulanbay.pms.web.bean.request.life;

import cn.mulanbay.common.aop.BindUser;

/**
 * ${DESCRIPTION}
 *
 * @author fenghong
 * @create 2018-02-19 09:43
 */
public class LifeExperienceGetStartCityTreeRequest implements BindUser{

    private Long userId;

    private Boolean needRoot;

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getNeedRoot() {
        return needRoot;
    }

    public void setNeedRoot(Boolean needRoot) {
        this.needRoot = needRoot;
    }
}
