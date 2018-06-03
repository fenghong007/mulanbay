package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.persistent.domain.PriceRegion;
import cn.mulanbay.pms.web.bean.request.CommonBeanDeleteRequest;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.buy.PriceRegionFormRequest;
import cn.mulanbay.pms.web.bean.request.buy.PriceRegionSearch;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
@RequestMapping("/priceRegion")
public class PriceRegionController extends BaseController {

	private static Class<PriceRegion> beanClass = PriceRegion.class;

	@RequestMapping(value = "list")
	public String list() {
		return "buy/priceRegionList";
	}

	/**
	 * 获取任务列表
	 * @return
	 */
	@RequestMapping(value = "/getData", method = RequestMethod.GET)
	@ResponseBody
	public ResultBean getData(PriceRegionSearch sf) {
		return callbackDataGrid(getPriceRegionResult(sf));
	}

	private PageResult<PriceRegion> getPriceRegionResult(PriceRegionSearch sf){
		PageRequest pr = sf.buildQuery();
		pr.setBeanClass(beanClass);
		Sort sort =new Sort("orderIndex",Sort.ASC);
		pr.addSort(sort);
		PageResult<PriceRegion> qr = baseService.getBeanResult(pr);
		return qr;
	}

	/**
	 * 创建
	 *
	 * @return
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean create(@Valid PriceRegionFormRequest formRequest) {
		PriceRegion bean = new PriceRegion();
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
		PriceRegion bean=baseService.getObjectWithUser(PriceRegion.class,getRequest.getId().intValue(),getRequest.getUserId());
		return callback(bean);
	}

	/**
	 * 修改
	 *
	 * @return
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean edit(@Valid PriceRegionFormRequest formRequest) {
		PriceRegion bean=baseService.getObjectWithUser(PriceRegion.class,formRequest.getId(),formRequest.getUserId());
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
		this.deleteUserEntity(beanClass,
				NumberUtil.stringArrayToIntegerArray(deleteRequest.getIds().split(",")),
				deleteRequest.getUserId());
		return callback(null);
	}


}
