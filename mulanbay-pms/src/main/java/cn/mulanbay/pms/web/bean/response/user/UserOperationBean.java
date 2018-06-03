package cn.mulanbay.pms.web.bean.response.user;

import java.util.Date;

/**
 * ${DESCRIPTION}
 * 用户操作
 *
 * @author fenghong
 * @create 2018-02-18 08:45
 */
public class UserOperationBean {

    private Date occurTime;

    private String content;

    public Date getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(Date occurTime) {
        this.occurTime = occurTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
