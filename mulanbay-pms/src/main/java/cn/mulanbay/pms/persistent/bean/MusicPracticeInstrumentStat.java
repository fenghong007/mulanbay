package cn.mulanbay.pms.persistent.bean;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 练习乐器练习
 */
public class MusicPracticeInstrumentStat {

	private BigInteger id;

	private String name;

	private BigInteger totalCount;
	
	private BigDecimal totalMinutes;

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigInteger getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(BigInteger totalCount) {
		this.totalCount = totalCount;
	}

	public BigDecimal getTotalMinutes() {
		return totalMinutes;
	}

	public void setTotalMinutes(BigDecimal totalMinutes) {
		this.totalMinutes = totalMinutes;
	}
}
