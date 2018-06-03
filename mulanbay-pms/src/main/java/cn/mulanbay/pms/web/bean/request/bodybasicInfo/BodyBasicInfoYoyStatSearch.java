package cn.mulanbay.pms.web.bean.request.bodybasicInfo;

import cn.mulanbay.common.aop.BindUser;
import cn.mulanbay.pms.web.bean.request.BaseYoyStatSearch;

/**
 * Created by fenghong on 2017/2/5.
 * 身体基本情况同期比对
 */
public class BodyBasicInfoYoyStatSearch extends BaseYoyStatSearch implements BindUser {

    private Long userId;

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
