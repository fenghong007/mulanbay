package cn.mulanbay.pms.web.controller;

import cn.mulanbay.business.handler.MessageHandler;
import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.exception.ValidateError;
import cn.mulanbay.common.util.DateUtil;
import cn.mulanbay.common.util.IPAddressUtil;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.service.BaseService;
import cn.mulanbay.pms.common.PmsErrorCode;
import cn.mulanbay.pms.handler.LoginCheckHandler;
import cn.mulanbay.pms.persistent.enums.DateGroupType;
import cn.mulanbay.pms.util.Constant;
import cn.mulanbay.pms.web.bean.request.BaseYoyStatSearch;
import cn.mulanbay.pms.web.bean.request.DateStatSearch;
import cn.mulanbay.pms.web.bean.response.DataGrid;
import cn.mulanbay.pms.web.bean.response.chart.ChartData;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
 * 
 * @author fenghong
 * 
 */
public class BaseController {

	@Autowired
	protected HttpServletRequest request;

	@Autowired
	protected BaseService baseService;

	@Autowired
	protected MessageHandler messageHandler;

	@Autowired
	protected LoginCheckHandler loginCheckHandler;

	// # 统计图里面的子标题是否需要总的统计值
	@Value("${system.chart.subTitle.hasTotal}")
	boolean chartSubTitleHasTotal;

	private static ResultBean defaultResultBean = new ResultBean();

	public HttpSession getSeesion() {
		return request.getSession();
	}

	public String getSessionId() {
		return getSeesion().getId();
	}

	protected Long getCurrentUserId() {
		return loginCheckHandler.getLoginUserIdEnhanced(request);
	}

	/**
	 * 跟easyui结合
	 *
	 * @param pr
	 * @return
	 */
	protected ResultBean callbackDataGrid(PageResult<?> pr) {
		ResultBean rb = new ResultBean();
		DataGrid dg = new DataGrid();
		dg.setTotal(pr.getMaxRow());
		dg.setRows(pr.getBeanList());
		rb.setData(dg);
		return rb;
	}


	protected ResultBean callback(Object o) {
		if (o == null) {
			return defaultResultBean;
		}
		ResultBean rb = new ResultBean();
		rb.setData(o);
		return rb;
	}

	/**
	 * 直接返回错误代码
	 *
	 * @param errorCode
	 * @return
	 */
	protected ResultBean callbackErrorCode(int errorCode) {
		ResultBean rb = new ResultBean();
		rb.setCode(errorCode);
		ValidateError ve = messageHandler.getErrorCodeInfo(errorCode);
		rb.setMessage(ve.getErrorInfo());
		return rb;
	}

	/**
	 * 直接返回错误信息
	 *
	 * @param msg
	 * @return
	 */
	protected ResultBean callbackErrorInfo(String msg) {
		ResultBean rb = new ResultBean();
		rb.setCode(ErrorCode.DO_BUSS_ERROR);
		rb.setMessage(msg);
		return rb;
	}


	protected String getIpAddress() {
		return IPAddressUtil.getIpAddress(request);
	}

	/**
	 * 获取日期的标题，只要用于报表的子标题
	 *
	 * @param sf
	 * @return
	 */
	protected String getDateTitle(DateStatSearch sf) {
		if (sf.getStartDate() == null && sf.getEndDate() == null) {
			return "";
		} else if (sf.getStartDate() != null && sf.getEndDate() == null) {
			return "从" + DateUtil.getFormatDate(sf.getStartDate(), DateUtil.FormatDay1) + "开始";
		} else if (sf.getStartDate() == null && sf.getEndDate() != null) {
			return "截止" + DateUtil.getFormatDate(sf.getEndDate(), DateUtil.FormatDay1);
		} else {
			return DateUtil.getFormatDate(sf.getStartDate(), DateUtil.FormatDay1) + "~" +
					DateUtil.getFormatDate(sf.getEndDate(), DateUtil.FormatDay1);
		}
	}

	/**
	 * 获取日期的标题，只要用于报表的子标题
	 *
	 * @param sf
	 * @param total
	 * @return
	 */
	protected String getDateTitle(DateStatSearch sf, String total) {
		String dateTitle = this.getDateTitle(sf);
		if (chartSubTitleHasTotal) {
			if (!dateTitle.isEmpty()) {
				dateTitle += "\n";
			}
			dateTitle += "Total:" + total;
		}
		return dateTitle;
	}

	/**
	 * 初始化同期对比数据
	 *
	 * @param sf
	 * @param title
	 * @param subTitle
	 * @return
	 */
	protected ChartData initYoyCharData(BaseYoyStatSearch sf, String title, String subTitle) {
		ChartData chartData = new ChartData();
		chartData.setTitle(title);
		chartData.setSubTitle(subTitle);
		if (sf.getDateGroupType() == DateGroupType.MONTH) {
			for (int i = 1; i <= Constant.MAX_MONTH; i++) {
				chartData.getIntXData().add(i);
				chartData.getXdata().add(i + "月份");
			}
		} else if (sf.getDateGroupType() == DateGroupType.WEEK) {
			for (int i = 1; i <= Constant.MAX_WEEK; i++) {
				chartData.getIntXData().add(i);
				chartData.getXdata().add("第" + i + "周");
			}
		}
		return chartData;
	}

	/**
	 * 获取用户的数据
	 * 查找实体时id和userId同时绑定
	 * todo 后期可以根据当前用户身份，如果是管理员则直接根据id查询
	 *
	 * @param c
	 * @param id
	 * @param userId
	 * @param <T>
	 * @return
	 */
	protected <T> T getUserEntity(Class<T> c, Serializable id, Long userId) {
		T bean = baseService.getObjectWithUser(c, id, userId);
		if(bean==null){
			// 找不到直接抛异常
			throw new ApplicationException(PmsErrorCode.USER_ENTITY_NOT_FOUND);
		}
		return bean;
	}

	/**
	 * 删除用户数据
	 * @param c
	 * @param ids
	 * @param userId
	 */
	protected void deleteUserEntity(Class c, Serializable[] ids, Long userId) {
		baseService.deleteObjectsWithUser(c, ids, userId);
	}
}