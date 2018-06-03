package cn.mulanbay.web.bean.request;

import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.QueryBulider;

/**
 * 查询列表通用基类
 *
 * @author fenghong
 * @create 2018-01-20 21:44
 */
public class PageSearch extends QueryBulider {

	// 页码
	protected Integer page;

	// 单页显示条数
	protected Integer pageSize;

	// 针对easyui设计
	protected Integer rows;

	public Integer getPage() {
		if (page == null) {
			return 1;
		}
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		// 设置默认值
		if (pageSize == null) {
			if (rows == null) {
				return 20;
			} else {
				return rows;
			}
		} else {
			return pageSize;
		}
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	/**
	 * 支持分页
	 * @return
	 */
	@Override
	public PageRequest buildQuery() {
		PageRequest pg = super.buildQuery();
		pg.setPage(this.getPage());
		pg.setPageSize(this.getPageSize());
		return pg;
	}
}
