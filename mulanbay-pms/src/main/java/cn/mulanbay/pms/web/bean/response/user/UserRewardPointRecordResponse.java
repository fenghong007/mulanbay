package cn.mulanbay.pms.web.bean.response.user;

import cn.mulanbay.pms.persistent.domain.UserRewardPointRecord;

/**
 * ${DESCRIPTION}
 *
 * @author fenghong
 * @create 2018-02-16 10:27
 */
public class UserRewardPointRecordResponse extends UserRewardPointRecord {

    private String sourceName;

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getRewardSourceName(){
        return this.getRewardSource()==null ? null: this.getRewardSource().getName();
    }
}
