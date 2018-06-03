package cn.mulanbay.pms.web.bean.response.dream;

/**
 * Created by fenghong on 2017/1/23.
 * 由于无法使用jar里面的对象，只能自己拼接数据，界面上显示js
 */
public class BarData {

    private String[] xdata;

    private Object[] ydata;

    public String[] getXdata() {
        return xdata;
    }

    public void setXdata(String[] xdata) {
        this.xdata = xdata;
    }

    public Object[] getYdata() {
        return ydata;
    }

    public void setYdata(Object[] ydata) {
        this.ydata = ydata;
    }
}
