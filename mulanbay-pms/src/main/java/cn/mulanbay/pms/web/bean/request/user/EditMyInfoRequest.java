package cn.mulanbay.pms.web.bean.request.user;

import cn.mulanbay.common.aop.BindUser;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * ${DESCRIPTION}
 *
 * @author fenghong
 * @create 2018-03-29 20:23
 */
public class EditMyInfoRequest implements BindUser {

    private Long userId;

    @NotEmpty(message = "{validate.user.username.notEmpty}")
    private String username;

    @NotEmpty(message = "{validate.user.nickname.notEmpty}")
    private String nickname;
    private String password;

    @NotEmpty(message = "{validate.user.phone.notEmpty}")
    private String phone;
    //邮件发送
    @NotEmpty(message = "{validate.user.email.notEmpty}")
    private String email;
    //生日（计算最大心率使用到）
    @NotNull(message = "{validate.user.birthday.NotNull}")
    private Date birthday;

    @NotNull(message = "{validate.user.sendEmail.NotNull}")
    private Boolean sendEmail;

    @NotNull(message = "{validate.user.sendWxMessage.NotNull}")
    private Boolean sendWxMessage;

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Boolean getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(Boolean sendEmail) {
        this.sendEmail = sendEmail;
    }

    public Boolean getSendWxMessage() {
        return sendWxMessage;
    }

    public void setSendWxMessage(Boolean sendWxMessage) {
        this.sendWxMessage = sendWxMessage;
    }
}
