package cn.mulanbay.pms.web.bean.request;

import cn.mulanbay.common.aop.BindUser;
import org.hibernate.validator.constraints.NotEmpty;

public class CommonBeanDeleteRequest implements BindUser {

    @NotEmpty(message = "{validate.bean.ids.notNull}")
    private String ids;

    private Long userId;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
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
