package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.persistent.domain.CommonRecordType;
import cn.mulanbay.pms.persistent.enums.CommonStatus;
import cn.mulanbay.pms.persistent.service.CommonRecordService;
import cn.mulanbay.pms.util.TreeBeanUtil;
import cn.mulanbay.pms.web.bean.request.CommonBeanDeleteRequest;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.commonrecord.CommonRecordTypeFormRequest;
import cn.mulanbay.pms.web.bean.request.commonrecord.CommonRecordTypeSearch;
import cn.mulanbay.pms.web.bean.response.TreeBean;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用记录类型
 */
@Controller
@RequestMapping("/commonRecordType")
public class CommonRecordTypeController extends BaseController {

	private static Class<CommonRecordType> beanClass = CommonRecordType.class;


	@Autowired
	CommonRecordService commonRecordService;
	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getCommonRecordTypeTree")
	@ResponseBody
	public ResultBean getCommonRecordTypeTree(CommonRecordTypeSearch sf) {
		try {
			sf.setStatus(CommonStatus.ENABLE);
			PageResult<CommonRecordType> pr = getCommonRecordTypeResult(sf);
			List<TreeBean> list = new ArrayList<TreeBean>();
			List<CommonRecordType> gtList = pr.getBeanList();
			for (CommonRecordType gt : gtList) {
				TreeBean tb = new TreeBean();
				tb.setId(gt.getId().toString());
				tb.setText(gt.getName());
				list.add(tb);
			}
			return callback(TreeBeanUtil.addRoot(list,sf.getNeedRoot()));
		} catch (Exception e) {
			throw new ApplicationException(ErrorCode.SYSTEM_ERROR, "通用记录类型异常",
					e);
		}
	}

	@RequestMapping(value = "list")
	public String list() {
		return "data/commonRecordTypeList";
	}

	/**
	 * 获取任务列表
	 * @return
	 */
	@RequestMapping(value = "/getData", method = RequestMethod.GET)
	@ResponseBody
	public ResultBean getData(CommonRecordTypeSearch sf) {
		return callbackDataGrid(getCommonRecordTypeResult(sf));
	}

	private PageResult<CommonRecordType> getCommonRecordTypeResult(CommonRecordTypeSearch sf){
		PageRequest pr = sf.buildQuery();
		pr.setBeanClass(beanClass);
		Sort sort =new Sort("orderIndex",Sort.ASC);
		pr.addSort(sort);
		PageResult<CommonRecordType> qr = baseService.getBeanResult(pr);
		return qr;
	}

	/**
	 * 创建
	 *
	 * @return
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean create(@Valid CommonRecordTypeFormRequest formRequest) {
		CommonRecordType bean = new CommonRecordType();
		BeanCopy.copyProperties(formRequest,bean);
		baseService.saveObject(bean);
		return callback(null);
	}


	/**
	 * 创建
	 *
	 * @return
	 */
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public ResultBean get(@Valid CommonBeanGetRequest getRequest) {
		CommonRecordType bean = this.getUserEntity(CommonRecordType.class, getRequest.getId().intValue(),getRequest.getUserId());
		return callback(bean);
	}

	/**
	 * 修改
	 *
	 * @return
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean edit(@Valid CommonRecordTypeFormRequest formRequest) {
		CommonRecordType bean = this.getUserEntity(CommonRecordType.class, formRequest.getId(),formRequest.getUserId());
		BeanCopy.copyProperties(formRequest,bean);
		baseService.updateObject(bean);
		return callback(null);
	}

	/**
	 * 删除
	 *
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@ResponseBody
	public ResultBean delete(@Valid CommonBeanDeleteRequest deleteRequest) {
		String[] ids = deleteRequest.getIds().split(",");
		for(String s : ids){
			CommonRecordType bean = this.getUserEntity(CommonRecordType.class, Integer.valueOf(s),deleteRequest.getUserId());
			commonRecordService.deleteCommonRecordType(bean);

		}
		return callback(null);
	}


}
