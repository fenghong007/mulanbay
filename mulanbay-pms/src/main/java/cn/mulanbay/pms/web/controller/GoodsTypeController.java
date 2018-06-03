package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.exception.ApplicationException;
import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.util.BeanCopy;
import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.persistent.domain.GoodsType;
import cn.mulanbay.pms.persistent.enums.CommonStatus;
import cn.mulanbay.pms.util.TreeBeanUtil;
import cn.mulanbay.pms.web.bean.request.CommonBeanDeleteRequest;
import cn.mulanbay.pms.web.bean.request.CommonBeanGetRequest;
import cn.mulanbay.pms.web.bean.request.buy.GoodsTreeSearch;
import cn.mulanbay.pms.web.bean.request.buy.GoodsTypeFormRequest;
import cn.mulanbay.pms.web.bean.request.buy.GoodsTypeSearch;
import cn.mulanbay.pms.web.bean.response.TreeBean;
import cn.mulanbay.pms.web.bean.response.buy.GoodsTypeResponse;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/goodsType")
public class GoodsTypeController extends BaseController {

	private static Class<GoodsType> beanClass = GoodsType.class;

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getGoodsTypeTree")
	@ResponseBody
	public ResultBean getGoodsTypeTree(GoodsTreeSearch goodsTreeSearch) {

		try {
			GoodsTypeSearch sf = new GoodsTypeSearch();
			sf.setPid(goodsTreeSearch.getPid());
			sf.setStatus(CommonStatus.ENABLE);
			sf.setUserId(goodsTreeSearch.getUserId());
			PageResult<GoodsType> pr = getGoodsTypeResult(sf);
			List<TreeBean> list = new ArrayList<TreeBean>();
			List<GoodsType> gtList = pr.getBeanList();
			for (GoodsType gt : gtList) {
				TreeBean tb = new TreeBean();
				tb.setId(gt.getId().toString());
				tb.setText(gt.getName());
				list.add(tb);
			}
			GoodsTreeSearch.RootType rootType = goodsTreeSearch.getRootType();
			if(rootType==null||rootType== GoodsTreeSearch.RootType.COMMON){
				return callback(TreeBeanUtil.addRoot(list,goodsTreeSearch.getNeedRoot()));
			}else{
				TreeBean root = new TreeBean();
				root.setId("0");
				root.setText("根");
				root.setChildren(list);
				List<TreeBean> newList = new ArrayList<TreeBean>();
				newList.add(root);
				return callback(newList);
			}
		} catch (Exception e) {
			throw new ApplicationException(ErrorCode.SYSTEM_ERROR, "获取商品类型树异常",
					e);
		}
	}

	@RequestMapping(value = "list")
	public String list() {
		return "buy/goodsTypeList";
	}

	/**
	 * 获取商品类型树（前面接口采用树形结构）
	 * @return
	 */
	@RequestMapping(value = "/getData", method = RequestMethod.GET)
	@ResponseBody
	public ResultBean getData(GoodsTypeSearch sf) {
		//先获取第一级目录
		sf.setPid(0);
		PageResult<GoodsType> pageResult = getGoodsTypeResult(sf);
		List<GoodsTypeResponse> parentGoodsType = new ArrayList<>();
		for(GoodsType gt: pageResult.getBeanList()){
			GoodsTypeResponse response = new GoodsTypeResponse();
			BeanCopy.copyProperties(gt,response);
			response.setParentName(gt.getParentName());
			//获取子商品类型
			sf.setPid(gt.getId());
			PageResult<GoodsType> childrenResult = getGoodsTypeResult(sf);
			if(childrenResult.getMaxRow()>0){
				List<GoodsTypeResponse> children = new ArrayList<>();
				for(GoodsType cc : childrenResult.getBeanList()){
					GoodsTypeResponse child = new GoodsTypeResponse();
					BeanCopy.copyProperties(cc,child);
					child.setParentName(cc.getParentName());
					children.add(child);
				}
				response.setChildren(children);
			}
			parentGoodsType.add(response);
		}
		PageResult<GoodsTypeResponse> result = new PageResult();
		result.setBeanList(parentGoodsType);
		result.setMaxRow(pageResult.getMaxRow());
		result.setPageSize(pageResult.getPageSize());
		return callbackDataGrid(result);
	}

	private PageResult<GoodsType> getGoodsTypeResult(GoodsTypeSearch sf){
		PageRequest pr = sf.buildQuery();
		pr.setBeanClass(beanClass);
		Sort sort =new Sort("orderIndex",Sort.ASC);
		pr.addSort(sort);
		PageResult<GoodsType> qr = baseService.getBeanResult(pr);
		return qr;
	}

	/**
	 * 创建
	 *
	 * @return
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean create(@Valid GoodsTypeFormRequest bean) {
		GoodsType goodsType = new GoodsType();
		BeanCopy.copyProperties(bean,goodsType);
		baseService.saveObject(goodsType);
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
		GoodsType bean=this.getUserEntity(beanClass, getRequest.getId().intValue(),getRequest.getUserId());
		return callback(bean);
	}

	/**
	 * 修改
	 *
	 * @return
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean edit(@Valid GoodsTypeFormRequest formRequest) {
		GoodsType bean=this.getUserEntity(beanClass, formRequest.getId().intValue(),formRequest.getUserId());
		BeanCopy.copyProperties(bean,bean);
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
