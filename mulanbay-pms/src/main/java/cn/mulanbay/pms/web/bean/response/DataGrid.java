package cn.mulanbay.pms.web.bean.response;

public class DataGrid {

	private long total;

	private Object rows;

	public DataGrid() {
		super();
	}

	public DataGrid(long total, Object rows) {
		super();
		this.total = total;
		this.rows = rows;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public Object getRows() {
		return rows;
	}

	public void setRows(Object rows) {
		this.rows = rows;
	}
	
	
}
