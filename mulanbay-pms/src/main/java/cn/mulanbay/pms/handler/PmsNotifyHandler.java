package cn.mulanbay.pms.handler;

import cn.mulanbay.business.handler.BaseHandler;
import cn.mulanbay.common.util.DateUtil;
import cn.mulanbay.common.util.StringUtil;
import cn.mulanbay.persistent.service.BaseService;
import cn.mulanbay.pms.persistent.domain.SystemMonitorUser;
import cn.mulanbay.pms.persistent.domain.User;
import cn.mulanbay.pms.persistent.domain.UserMessage;
import cn.mulanbay.pms.persistent.domain.UserSetting;
import cn.mulanbay.pms.persistent.enums.LogLevel;
import cn.mulanbay.pms.persistent.enums.MessageSendStatus;
import cn.mulanbay.pms.persistent.enums.MessageType;
import cn.mulanbay.pms.persistent.enums.MonitorBussType;
import cn.mulanbay.pms.persistent.service.AuthService;
import cn.mulanbay.pms.thread.UserMessageSendThread;
import cn.mulanbay.schedule.NotifiableProcessor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * 提醒处理
 *
 * @author fenghong
 * @create 2017-07-10 21:44
 */
@Component
public class PmsNotifyHandler extends BaseHandler implements NotifiableProcessor {

    private static Logger logger = Logger.getLogger(PmsNotifyHandler.class);

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    // 发送邮件的服务器的IP和端口
    @Value("${mail.mailServerHost}")
    private String mailServerHost;

    @Value("${mail.mailServerPort}")
    private String mailServerPort = "587";

    @Value("${system.nodeId}")
    private String nodeId;

    @Value("${notify.message.expectSendTime}")
    String expectSendTime;

    static Properties props = null;

    @Autowired
    BaseService baseService;

    @Autowired
    ThreadPoolHandler threadPoolHandler;

    @Autowired
    AuthService authService;

    @Autowired
    WxpayHandler wxpayHandler;

    @Override
    public void init() {
        super.init();
        props = new Properties();
        // 表示SMTP发送邮件，必须进行身份验证
        props.put("mail.smtp.auth", "true");
        //此处填写SMTP服务器
        props.put("mail.smtp.host", mailServerHost);
        //端口号，QQ邮箱给出了两个端口，但是另一个我一直使用不了，所以就给出这一个587
        props.put("mail.smtp.port", mailServerPort);
        // 此处填写你的账号
        props.put("mail.user", username);
        // 此处的密码就是前面说的16位STMP口令
        props.put("mail.password", password);
    }

    public PmsNotifyHandler() {
        super("提醒处理");
    }

    /**
     * 添加消息
     * @param title
     * @param content
     * @param userId
     * @param expectSendTime
     */
    public Long addNotifyMessage(String title,String content,Long userId,Date expectSendTime){
        UserMessage message = new UserMessage();
        //是否需要实时发送
        boolean sendImit=false;
        if(expectSendTime!=null){
            message.setExpectSendTime(expectSendTime);
        }else {
            sendImit=true;
            message.setExpectSendTime(new Date());
        }
        message.setUserId(userId);
        message.setContent(content);
        message.setCreatedTime(new Date());
        message.setFailCount(0);
        //没有作用
        message.setMessageType(MessageType.WX);
        message.setBussType(MonitorBussType.SYSTEM);
        message.setLogLevel(LogLevel.WARNING);
        message.setTitle(title);
        if(sendImit){
            this.sendMessage(message);
        }else{
            message.setSendStatus(MessageSendStatus.UN_SEND);
            if(message.getNodeId()==null){
                message.setNodeId(nodeId);
            }
            baseService.saveObject(message);
        }
        return message.getId();
    }

    private boolean sendMail(String title,String content,Long userId){
        User user = baseService.getObject(User.class,userId);
        return this.sendMail(title,content,user);

    }

    private boolean sendMail(String title,String content,User user){
        return this.sendMail(title,content,user.getEmail());
    }

    /**
     * 发送消息(异步)
     * @param message
     * @return
     */
    public void sendMessageAsync(UserMessage message){
        UserMessageSendThread thread = new UserMessageSendThread(message,this);
        threadPoolHandler.pushThread(thread);
    }
    /**
     * 发送消息
     * @param message
     * @return
     */
    public boolean sendMessage(UserMessage message){
        try {
            User user = baseService.getObject(User.class,message.getUserId());
            UserSetting userSetting = baseService.getObject(UserSetting.class,message.getUserId());
            if(user==null||userSetting==null){
                logger.warn("无法获取到userId="+message.getUserId()+"用户信息,无法发送消息");
                message.setSendStatus(MessageSendStatus.SEND_SUCCESS);
                message.setFailCount(message.getFailCount() + 1);
                message.setLastSendTime(new Date());
                baseService.saveOrUpdateObject(message);
                return true;
            }
            boolean b=false;
            if(userSetting.getSendEmail()&& StringUtil.isNotEmpty(user.getEmail())){
                // 发送邮件
               b= this.sendMail(message.getTitle(),message.getContent(),user.getEmail());
            }
            if(userSetting.getSendWxMessage()){
                b = wxpayHandler.sendTemplateMessage(message.getUserId(),message.getTitle(),message.getContent(),message.getCreatedTime());
            }
            if (b) {
                message.setSendStatus(MessageSendStatus.SEND_SUCCESS);
            } else {
                message.setSendStatus(MessageSendStatus.SEND_FAIL);
                message.setFailCount(message.getFailCount() + 1);
            }
            message.setLastSendTime(new Date());
            baseService.saveOrUpdateObject(message);
            return b;
        } catch (Exception e) {
            logger.error("发送消息失败，id=" + message.getId(), e);
            return false;
        }

    }

    /**
     * 发送邮件
     * @param title
     * @param content
     * @param toAddress
     * @return
     */
    private boolean sendMail(String title,String content,String toAddress){
        try {
            // 构建授权信息，用于进行SMTP进行身份验证
            Authenticator authenticator = new Authenticator() {

                protected PasswordAuthentication getPasswordAuthentication() {
                    // 用户名、密码
                    String userName = props.getProperty("mail.user");
                    String password = props.getProperty("mail.password");
                    return new PasswordAuthentication(userName, password);
                }
            };
            // 使用环境属性和授权信息，创建邮件会话
            Session mailSession = Session.getInstance(props, authenticator);
            // 创建邮件消息
            MimeMessage message = new MimeMessage(mailSession);
            // 设置发件人
            InternetAddress form = new InternetAddress(
                    props.getProperty("mail.user"));
            message.setFrom(form);

            // 设置收件人的邮箱
            InternetAddress to = new InternetAddress(toAddress);
            message.setRecipient(MimeMessage.RecipientType.TO, to);

            // 设置邮件标题
            message.setSubject(title);

            // 设置邮件的内容体
            message.setContent(content, "text/html;charset=UTF-8");

            // 最后当然就是发送邮件啦
            Transport.send(message);
            logger.debug("向"+toAddress+"发送了一封邮件");
            return true;
        } catch (Exception e) {
            logger.error("向["+toAddress+"]发送邮件异常",e);
            return false;
        }
    }

    /**
     * 向系统中需要通知的人发送系统消息
     * @param title
     * @param content
     * @param notifyTime
     */
    public void addMessageToNotifier(String title, String content, Date notifyTime , LogLevel level, String remark, MonitorBussType bussType){
        try {
            // 是否要及时发送
            boolean sendImit = false;
            if(notifyTime==null){
                notifyTime =DateUtil.getDate((DateUtil.getToday()+" "+expectSendTime+":00"),DateUtil.Format24Datetime);
            }else{
                sendImit=true;
            }
            List<SystemMonitorUser> userList = authService.selectSystemMonitorUserList(bussType);
            if(StringUtil.isEmpty(userList)){
                logger.warn("业务类型["+bussType+"]没有配置系统监控人员");
                return;
            }
            for(SystemMonitorUser smu : userList){
                UserMessage ssm = new UserMessage();
                ssm.setBussType(bussType);
                ssm.setContent(content);
                ssm.setCreatedTime(new Date());
                ssm.setExpectSendTime(notifyTime);
                ssm.setFailCount(0);
                ssm.setLogLevel(level);
                ssm.setMessageType(MessageType.WX);
                ssm.setRemark(remark);
                ssm.setTitle(title);
                ssm.setUserId(smu.getUserId());
                if(sendImit){
                    //立即发送
                    boolean b = this.sendMessage(ssm);
                    if(b){
                        ssm.setSendStatus(MessageSendStatus.SEND_SUCCESS);
                    }else{
                        ssm.setSendStatus(MessageSendStatus.SEND_FAIL);
                    }
                }else{
                    ssm.setSendStatus(MessageSendStatus.UN_SEND);
                }
                baseService.saveObject(ssm);
            }
        } catch (Exception e) {
            logger.error("发送系统消息异常",e);
        }
    }

    @Override
    public void notifyMessage(Long taskTriggerId, String title, String message) {

    }
}
