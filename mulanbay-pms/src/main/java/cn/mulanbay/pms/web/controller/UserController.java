package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.common.util.StringUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.handler.LoginCheckHandler;
import cn.mulanbay.pms.persistent.domain.User;
import cn.mulanbay.pms.persistent.domain.UserSetting;
import cn.mulanbay.pms.persistent.service.AuthService;
import cn.mulanbay.pms.util.TreeBeanUtil;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.auth.UserFormRequest;
import cn.mulanbay.pms.web.bean.request.auth.UserSearch;
import cn.mulanbay.pms.web.bean.request.user.EditMyInfoRequest;
import cn.mulanbay.pms.web.bean.response.TreeBean;
import cn.mulanbay.pms.web.bean.response.user.UserInfoResponse;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

	private static Class<User> beanClass = User.class;

	@Autowired
    AuthService authService;

	@Autowired
    LoginCheckHandler loginCheckHandler;

	@RequestMapping(value = "list")
	public String list() {
		return "user/userList";
	}

	/**
	 *
	 * @return
	 */
	@RequestMapping(value = "/getUserTree")
	@ResponseBody
	public ResultBean getUserTree(Boolean needRoot) {
		try {
			UserSearch sf = new UserSearch();
			PageResult<User> pageResult = getUserResult(sf);
			List<TreeBean> list = new ArrayList<TreeBean>();
			List<User> gtList = pageResult.getBeanList();
			for (User gt : gtList) {
				TreeBean tb = new TreeBean();
				tb.setId(gt.getId().toString());
				tb.setText(gt.getUsername());
				list.add(tb);
			}
			return callback(TreeBeanUtil.addRoot(list,needRoot));
		} catch (Exception e) {
			throw new ApplicationException(ErrorCode.SYSTEM_ERROR, "获取购买来源树异常",
					e);
		}
	}

	/**
	 * 获取商品类型数
	 * @return
	 */
	@RequestMapping(value = "/getData", method = RequestMethod.GET)
	@ResponseBody
	public ResultBean getData(UserSearch sf) {
		PageResult<User> pageResult = getUserResult(sf);
		return callbackDataGrid(pageResult);
	}

	private PageResult<User> getUserResult(UserSearch sf){
		PageRequest pr = sf.buildQuery();
		pr.setBeanClass(beanClass);
		Sort sort =new Sort("createdTime",Sort.ASC);
		pr.addSort(sort);
		PageResult<User> qr = baseService.getBeanResult(pr);
		return qr;
	}

	/**
	 * 创建
	 *
	 * @return
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean create(@Valid UserFormRequest bean) {
		User user = new User();
		BeanCopy.copyProperties(bean,user);
		// 密码设置
		String encodePassword = loginCheckHandler.encodePassword(bean.getPassword());
		user.setPassword(encodePassword);
		user.setCreatedTime(new Date());
		baseService.saveObject(user);
		UserSetting us = new UserSetting();
		us.setUserId(user.getId());
		us.setSendEmail(true);
		us.setSendWxMessage(true);
		us.setCreatedTime(new Date());
		baseService.saveObject(us);
		return callback(null);
	}


	/**
	 * 获取
	 *
	 * @return
	 */
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public ResultBean get(@Valid CommonBeanGetRequest getRequest) {
		User br=baseService.getObject(User.class, getRequest.getId());
		return callback(br);
	}

	/**
	 * 修改
	 *
	 * @return
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean edit(@Valid UserFormRequest bean) {
		User user = baseService.getObject(User.class,bean.getId());
		String originalPawword = user.getPassword();
		BeanCopy.copyProperties(bean,user);
		String password = bean.getPassword();
		if(null!=password && !password.isEmpty()){
			// 密码设置
			String encodePassword = loginCheckHandler.encodePassword(bean.getPassword());
			user.setPassword(encodePassword);
		}else {
			user.setPassword(originalPawword);
		}
		user.setLastModifyTime(new Date());
		baseService.updateObject(user);
		return callback(null);
	}

	/**
	 * 删除
	 *
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@ResponseBody
	public ResultBean delete(String ids) {
		baseService.deleteObjects(User.class, NumberUtil.stringArrayToLongArray(ids.split(",")));
		return callback(null);
	}

	/**
	 * 获取用户信息
	 *
	 * @return
	 */
	@RequestMapping(value = "/getMyInfo", method = RequestMethod.GET)
	@ResponseBody
	public ResultBean getMyInfo() {
		Long userId = this.getCurrentUserId();
		User user=baseService.getObject(User.class, userId);
		UserSetting us = authService.getUserSetting(userId);
		UserInfoResponse response = new UserInfoResponse();
		BeanCopy.copyProperties(user,response);
		BeanCopy.copyProperties(us,response);
		return callback(response);
	}

	/**
	 * 获取用户信息
	 *
	 * @return
	 */
	@RequestMapping(value = "/editMyInfo", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean editMyInfo(@Valid EditMyInfoRequest eui) {
		User user=baseService.getObject(User.class, eui.getUserId());
		UserSetting us = authService.getUserSetting(eui.getUserId());
		user.setUsername(eui.getUsername());
		user.setNickname(eui.getNickname());
		user.setBirthday(eui.getBirthday());
		user.setPhone(eui.getPhone());
		user.setEmail(eui.getEmail());
		user.setLastModifyTime(new Date());
		if(!StringUtil.isEmpty(eui.getPassword())){
			// 密码设置
			String encodePassword = loginCheckHandler.encodePassword(eui.getPassword());
			user.setPassword(encodePassword);
		}
		baseService.updateObject(user);
		BeanCopy.copyProperties(eui,us);
		us.setLastModifyTime(new Date());
		baseService.updateObject(us);
		return callback(null);
	}
}
