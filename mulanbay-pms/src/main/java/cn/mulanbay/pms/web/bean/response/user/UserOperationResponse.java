package cn.mulanbay.pms.web.bean.response.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author fenghong
 * @create 2018-02-18 08:51
 */
public class UserOperationResponse {

    private int id;

    private String title;

    private List<UserOperationBean> operations = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<UserOperationBean> getOperations() {
        return operations;
    }

    public void setOperations(List<UserOperationBean> operations) {
        this.operations = operations;
    }

    /**
     * 添加操作
     * @param date
     * @param content
     */
    public void addUserOperation(Date date, String content){
        UserOperationBean uor = new UserOperationBean();
        uor.setOccurTime(date);
        uor.setContent(content);
        operations.add(uor);
    }
}
