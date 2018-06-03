package cn.mulanbay.pms.web.bean.request.health;

import cn.mulanbay.common.aop.BindUser;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * ${DESCRIPTION}
 *
 * @author fenghong
 * @create 2018-02-15 22:26
 */
public class TreatDrugDetailFormRequest implements BindUser{

    private Long id;

    @NotNull(message = "{validate.treatDrugDetail.treatDrugId.NotNull}")
    private Long treatDrugId;
    private Long userId;
    //用药时间
    @NotNull(message = "{validate.treatDrugDetail.occurTime.NotNull}")
    private Date occurTime;
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTreatDrugId() {
        return treatDrugId;
    }

    public void setTreatDrugId(Long treatDrugId) {
        this.treatDrugId = treatDrugId;
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(Date occurTime) {
        this.occurTime = occurTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
