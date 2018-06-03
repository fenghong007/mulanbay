package cn.mulanbay.pms.web.controller;

import cn.mulanbay.common.util.NumberUtil;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import cn.mulanbay.pms.persistent.domain.DatabaseClean;
import cn.mulanbay.pms.persistent.service.DatabaseCleanService;
import cn.mulanbay.pms.web.bean.request.data.DatabaseCleanSearch;
import cn.mulanbay.web.bean.response.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/databaseClean")
public class DatabaseCleanController extends BaseController {

	private static Class<DatabaseClean> beanClass = DatabaseClean.class;

	@Autowired
    DatabaseCleanService databaseCleanService;

	@RequestMapping(value = "list")
	public String list() {
		return "data/databaseCleanList";
	}

	/**
	 * 获取任务列表
	 * @return
	 */
	@RequestMapping(value = "/getData", method = RequestMethod.GET)
	@ResponseBody
	public ResultBean getData(DatabaseCleanSearch sf) {
		return callbackDataGrid(getDatabaseCleanResult(sf));
	}

	private PageResult<DatabaseClean> getDatabaseCleanResult(DatabaseCleanSearch sf){
		PageRequest pr = sf.buildQuery();
		pr.setBeanClass(beanClass);
		Sort sort =new Sort("orderIndex",Sort.ASC);
		pr.addSort(sort);
		PageResult<DatabaseClean> qr = baseService.getBeanResult(pr);
		return qr;
	}

	/**
	 * 创建
	 *
	 * @return
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean create(DatabaseClean bean) {
		bean.setCreatedTime(new Date());
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
	public ResultBean get(Long id) {
		DatabaseClean br=baseService.getObject(DatabaseClean.class, id);
		return callback(br);
	}

	/**
	 * 手动执行
	 *
	 * @return
	 */
	@RequestMapping(value = "/manualExec", method = RequestMethod.GET)
	@ResponseBody
	public ResultBean manualExec(Long id) {
		DatabaseClean br=baseService.getObject(DatabaseClean.class, id);
		databaseCleanService.updateAndExecDatabaseClean(br);
		return callback(null);
	}

	/**
	 * 修改
	 *
	 * @return
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean edit(DatabaseClean bean) {
		bean.setLastModifyTime(new Date());
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
	public ResultBean delete(String ids) {
		baseService.deleteObjects(DatabaseClean.class, NumberUtil.stringArrayToLongArray(ids.split(",")));
		return callback(null);
	}

}
