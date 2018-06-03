package cn.mulanbay.pms.web.bean.response.buy;

import cn.mulanbay.pms.persistent.enums.CommonStatus;

import java.util.List;

/**
 * Created by fenghong on 2017/3/5.
 * 商品类型，用来做treegrid使用
 */
public class GoodsTypeResponse {

    private Integer id;
    private String parentName;
    private String name;
    private CommonStatus status;
    private Short orderIndex;
    // 是否加入统计
    private Boolean statable;
    private List<GoodsTypeResponse> children;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Boolean getStatable() {
        return statable;
    }

    public void setStatable(Boolean statable) {
        this.statable = statable;
    }

    public List<GoodsTypeResponse> getChildren() {
        return children;
    }

    public void setChildren(List<GoodsTypeResponse> children) {
        this.children = children;
    }
}
