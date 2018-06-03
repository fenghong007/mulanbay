package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.persistent.domain.ConsumeType;
import cn.mulanbay.pms.persistent.enums.CommonStatus;
import cn.mulanbay.pms.util.TreeBeanUtil;
import cn.mulanbay.pms.web.bean.request.CommonBeanDeleteRequest;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.life.ConsumeTypeFormRequest;
import cn.mulanbay.pms.web.bean.request.life.ConsumeTypeSearch;
import cn.mulanbay.pms.web.bean.response.TreeBean;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/consumeType")
public class ConsumeTypeController extends BaseController {

	private static Class<ConsumeType> beanClass = ConsumeType.class;

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getConsumeTypeTree")
	@ResponseBody
	public ResultBean getConsumeTypeTree(ConsumeTypeSearch sf) {
		try {
			sf.setStatus(CommonStatus.ENABLE);
			PageResult<ConsumeType> pr = getConsumeTypeResult(sf);
			List<TreeBean> list = new ArrayList<TreeBean>();
			List<ConsumeType> gtList = pr.getBeanList();
			for (ConsumeType gt : gtList) {
				TreeBean tb = new TreeBean();
				tb.setId(gt.getId().toString());
				tb.setText(gt.getName());
				list.add(tb);
			}
			return callback(TreeBeanUtil.addRoot(list,sf.getNeedRoot()));
		} catch (Exception e) {
			throw new ApplicationException(ErrorCode.SYSTEM_ERROR, "获取购买来源树异常",
					e);
		}
	}

	@RequestMapping(value = "list")
	public String list() {
		return "life/consumeTypeList";
	}

	/**
	 * 获取任务列表
	 * @return
	 */
	@RequestMapping(value = "/getData", method = RequestMethod.GET)
	@ResponseBody
	public ResultBean getData(ConsumeTypeSearch sf) {
		return callbackDataGrid(getConsumeTypeResult(sf));
	}

	private PageResult<ConsumeType> getConsumeTypeResult(ConsumeTypeSearch sf){
		PageRequest pr = sf.buildQuery();
		pr.setBeanClass(beanClass);
		Sort sort =new Sort("orderIndex",Sort.ASC);
		pr.addSort(sort);
		PageResult<ConsumeType> qr = baseService.getBeanResult(pr);
		return qr;
	}

	/**
	 * 创建
	 *
	 * @return
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean create(@Valid ConsumeTypeFormRequest formRequest) {
		ConsumeType bean = new ConsumeType();
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
		ConsumeType bean=this.getUserEntity(ConsumeType.class, getRequest.getId().intValue(),getRequest.getUserId());
		return callback(bean);
	}

	/**
	 * 修改
	 *
	 * @return
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean edit(@Valid ConsumeTypeFormRequest formRequest) {
		ConsumeType bean=this.getUserEntity(ConsumeType.class, formRequest.getId(),formRequest.getUserId());
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
		this.deleteUserEntity(ConsumeType.class,
				NumberUtil.stringArrayToIntegerArray(deleteRequest.getIds().split(",")),
				deleteRequest.getUserId());
		return callback(null);
	}


}
