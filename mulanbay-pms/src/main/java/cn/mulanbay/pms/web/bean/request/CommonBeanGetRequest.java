package cn.mulanbay.pms.web.bean.request;

import cn.mulanbay.common.aop.BindUser;

import javax.validation.constraints.NotNull;

/**
 * 通用根据主键获取bean的请求
 */
public class CommonBeanGetRequest implements BindUser {

    @NotNull(message = "{validate.bean.id.notNull}")
    private Long id;

    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
