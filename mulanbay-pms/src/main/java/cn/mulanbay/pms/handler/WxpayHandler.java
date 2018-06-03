package cn.mulanbay.pms.handler;

import cn.mulanbay.business.handler.BaseHandler;
import cn.mulanbay.common.http.HttpResult;
import cn.mulanbay.common.http.HttpUtil;
import cn.mulanbay.common.util.DateUtil;
import cn.mulanbay.common.util.JsonUtil;
import cn.mulanbay.pms.handler.bean.MessageContent;
import cn.mulanbay.pms.handler.bean.WxAccessTocken;
import cn.mulanbay.pms.persistent.domain.UserWxpayInfo;
import cn.mulanbay.pms.persistent.service.AuthService;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * ${DESCRIPTION}
 *
 * @author fenghong
 * @create 2018-03-17 08:57
 */
@Component
public class WxpayHandler extends BaseHandler {

    private static Logger logger = Logger.getLogger(WxpayHandler.class);

    public WxpayHandler() {
        super("微信");
    }

    @Value("${wx.appId}")
    private String appId;

    @Value("${wx.secret}")
    private String secret;

    @Value("${wx.userMessageTemplateId}")
    private String userMessageTemplateId;

    @Autowired
    CacheHandler cacheHandler;

    @Autowired
    AuthService authService;

    public boolean sendTemplateMessage(Long userId,String title,String content,Date time){
        UserWxpayInfo uw = authService.getUserWxpayInfoForCache(userId,appId);
        if(uw==null){
            logger.warn("无法获取到userId="+userId+"的用户微信信息");
            return false;
        }
        MessageContent mc = new MessageContent();
        mc.setTouser(uw.getOpenId());
        mc.setTemplate_id(userMessageTemplateId);
        mc.addMessageData("title",title);
        mc.addMessageData("content","\n"+content);
        if(time!=null){
            mc.addMessageData("time",DateUtil.getFormatDate(time,DateUtil.Format24Datetime));
        }
        HttpResult hr = this.sendTemplateMessage(mc);
        if(hr.getStatusCode()==HttpStatus.SC_OK){
            return true;
        }else {
            logger.error("发送模板消息异常，"+hr.getResponseData());
            return false;
        }
    }

    /**
     * 发送模板消息
     * @param mc
     * @return
     */
    public HttpResult sendTemplateMessage(MessageContent mc){
        String accessTocken = getAccessToken();
        String url="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+accessTocken;
        String jsonData = JsonUtil.beanToJson(mc);
        HttpResult hr = HttpUtil.doPostJson(url,jsonData);
        return hr;
    }

    /**
     * 获取模板列表
     * @param accessTocken
     * @return
     */
    public HttpResult getTemplateList(String accessTocken){
        String url ="https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token="+accessTocken;
        HttpResult hr = HttpUtil.doHttpGet(url);
        return hr;
    }

    /**
     * 获取授权tocken
     *
     * @return
     */
    public String getAccessToken(){
        String accessToken = cacheHandler.getForString("wx:accessToken:"+appId);
        if(accessToken==null){
            // 获取accessTocken
            String url="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appId+"&secret="+secret;
            HttpResult hr = HttpUtil.doHttpGet(url);
            if(hr.getStatusCode()== HttpStatus.SC_OK){
                WxAccessTocken at = (WxAccessTocken) JsonUtil.jsonToBean(hr.getResponseData(),WxAccessTocken.class);
                accessToken = at.getAccess_token();
                cacheHandler.set("wx:accessToken:"+appId,accessToken,at.getExpires_in()-10);
            }else {
                logger.warn("无法获取到AccessToken");
            }
        }
        return accessToken;
    }

}
