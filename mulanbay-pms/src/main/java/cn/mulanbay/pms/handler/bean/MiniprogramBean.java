package cn.mulanbay.pms.handler.bean;

/**
 * ${DESCRIPTION}
 *
 * @author fenghong
 * @create 2018-03-17 09:00
 */
public class MiniprogramBean {

    /**
     * appid : xiaochengxuappid12345
     * pagepath : index?foo=bar
     */

    private String appid;
    private String pagepath;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPagepath() {
        return pagepath;
    }

    public void setPagepath(String pagepath) {
        this.pagepath = pagepath;
    }

}
