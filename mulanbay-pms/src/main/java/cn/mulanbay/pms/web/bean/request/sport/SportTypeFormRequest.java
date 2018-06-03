package cn.mulanbay.pms.web.bean.request.sport;

import cn.mulanbay.common.aop.BindUser;
import cn.mulanbay.pms.persistent.enums.CommonStatus;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * ${DESCRIPTION}
 *
 * @author fenghong
 * @create 2018-02-15 19:48
 */
public class SportTypeFormRequest implements BindUser{

    private Integer id;

    @NotEmpty(message = "{validate.sportType.name.notEmpty}")
    private String name;
    private Long userId;

    @NotNull(message = "{validate.sportType.status.NotNull}")
    private CommonStatus status;

    @NotNull(message = "{validate.sportType.orderIndex.NotNull}")
    private Short orderIndex;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public CommonStatus getStatus() {
        return status;
    }

    public void setStatus(CommonStatus status) {
        this.status = status;
    }

    public Short getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Short orderIndex) {
        this.orderIndex = orderIndex;
    }
}
