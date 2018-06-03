package cn.mulanbay.pms.web.bean.response.auth;

import java.util.List;

/**
 * Created by fenghong on 2017/9/9.
 */
public class SystemFunctionResponse {

    private Integer id;
    private String parentName;
    private String name;
    private List<SystemFunctionResponse> children;

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

    public List<SystemFunctionResponse> getChildren() {
        return children;
    }

    public void setChildren(List<SystemFunctionResponse> children) {
        this.children = children;
    }
}
