package cn.mulanbay.pms.web.bean.request.health;

import cn.mulanbay.common.aop.BindUser;
import cn.mulanbay.pms.web.bean.request.BaseYoyStatSearch;

/**
 * Created by fenghong on 2017/2/5.
 * 看病情况同期比对
 */
public class TreatRecordYoyStatSearch extends BaseYoyStatSearch implements BindUser{

    private Long userId;

    // 支持多个字段查询
    private String name;

    //需要统计的费用字段
    private String feeField;

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFeeField() {
        return feeField;
    }

    public void setFeeField(String feeField) {
        this.feeField = feeField;
    }
}
