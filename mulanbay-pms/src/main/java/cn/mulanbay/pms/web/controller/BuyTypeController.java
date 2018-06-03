package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.persistent.domain.BuyType;
import cn.mulanbay.pms.persistent.enums.CommonStatus;
import cn.mulanbay.pms.util.TreeBeanUtil;
import cn.mulanbay.pms.web.bean.request.buy.BuyTypeDeleteRequest;
import cn.mulanbay.pms.web.bean.request.buy.BuyTypeFormRequest;
import cn.mulanbay.pms.web.bean.request.buy.BuyTypeGetRequest;
import cn.mulanbay.pms.web.bean.request.buy.BuyTypeSearch;
import cn.mulanbay.pms.web.bean.response.TreeBean;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 购买类型（消费来源）
 *
 * @author fenghong
 * @create 2017-07-10 21:44
 */
@Controller
@RequestMapping("/buyType")
public class BuyTypeController extends BaseController {

	private static Class<BuyType> beanClass = BuyType.class;

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getBuyTypeTree")
	@ResponseBody
	public ResultBean getBuyTypeTree(BuyTypeSearch sf) {
		try {
			sf.setStatus(CommonStatus.ENABLE);
			PageResult<BuyType> pr = getBuyTypeResult(sf);
			List<TreeBean> list = new ArrayList<TreeBean>();
			List<BuyType> gtList = pr.getBeanList();
			for (BuyType gt : gtList) {
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
		return "buy/buyTypeList";
	}

	/**
	 * 获取任务列表
	 * @return
	 */
	@RequestMapping(value = "/getData", method = RequestMethod.GET)
	@ResponseBody
	public ResultBean getData(BuyTypeSearch sf) {
		return callbackDataGrid(getBuyTypeResult(sf));
	}

	private PageResult<BuyType> getBuyTypeResult(BuyTypeSearch sf){
		PageRequest pr = sf.buildQuery();
		pr.setBeanClass(beanClass);
		Sort sort =new Sort("orderIndex",Sort.ASC);
		pr.addSort(sort);
		PageResult<BuyType> qr = baseService.getBeanResult(pr);
		return qr;
	}

	/**
	 * 创建
	 *
	 * @return
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean create(@Valid BuyTypeFormRequest bean) {
		BuyType buyType = new BuyType();
		BeanCopy.copyProperties(bean,buyType);
		baseService.saveObject(buyType);
		return callback(null);
	}


	/**
	 * 创建
	 *
	 * @return
	 */
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public ResultBean get(@Valid BuyTypeGetRequest gr) {
		BuyType buyType = this.getUserEntity(beanClass,gr.getId(),gr.getUserId());
		return callback(buyType);
	}

	/**
	 * 修改
	 *
	 * @return
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean edit(@Valid BuyTypeFormRequest bean) {
		BuyType buyType = this.getUserEntity(beanClass,bean.getId(),bean.getUserId());
		BeanCopy.copyProperties(bean,buyType);
		baseService.updateObject(buyType);
		return callback(null);
	}

	/**
	 * 删除
	 *
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@ResponseBody
	public ResultBean delete(@Valid BuyTypeDeleteRequest bdr) {
		this.deleteUserEntity(BuyType.class, NumberUtil.stringArrayToIntegerArray(bdr.getIds().split(",")),bdr.getUserId());
		return callback(null);
	}

}
