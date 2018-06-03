package cn.mulanbay.pms.web.bean.request.schedule;

import java.util.Date;

public class TaskManualNewRequest {

    private Long taskTriggerId;

    private Date bussDate;

    private boolean isSync;

    public Long getTaskTriggerId() {
        return taskTriggerId;
    }

    public void setTaskTriggerId(Long taskTriggerId) {
        this.taskTriggerId = taskTriggerId;
    }

    public Date getBussDate() {
        return bussDate;
    }

    public void setBussDate(Date bussDate) {
        this.bussDate = bussDate;
    }

    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean sync) {
        isSync = sync;
    }
}
