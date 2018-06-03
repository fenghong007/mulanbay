package cn.mulanbay.pms.persistent.service;

import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.exception.PersistentException;
import cn.mulanbay.persistent.common.BaseException;
import cn.mulanbay.persistent.dao.BaseHibernateDao;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.pms.persistent.bean.*;
import cn.mulanbay.pms.persistent.domain.BuyRecord;
import cn.mulanbay.pms.persistent.domain.BuyType;
import cn.mulanbay.pms.persistent.domain.GoodsType;
import cn.mulanbay.pms.persistent.domain.PriceRegion;
import cn.mulanbay.pms.persistent.enums.BuyRecordPriceType;
import cn.mulanbay.pms.persistent.enums.DateGroupType;
import cn.mulanbay.pms.persistent.enums.MoneyFlow;
import cn.mulanbay.pms.persistent.enums.Payment;
import cn.mulanbay.pms.persistent.util.MysqlUtil;
import cn.mulanbay.pms.util.Constant;
import cn.mulanbay.pms.web.bean.request.ChartType;
import cn.mulanbay.pms.web.bean.request.GroupType;
import cn.mulanbay.pms.web.bean.request.buy.*;
import cn.mulanbay.pms.web.bean.request.life.BackHomeDateStatSearch;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class BuyRecordService extends BaseHibernateDao {

	/**
	 * 基本的统计
	 * @param sf
	 * @return
	 */
	public BuyRecordStat getBuyRecordStat(BuyRecordSearch sf) {
		try {
			PageRequest pr = sf.buildQuery();
			StringBuffer sb = new StringBuffer();
			sb.append("select count(*) as totalCount,sum(shipment) as totalShipment,sum(totalPrice) as totalPrice from BuyRecord ");
			sb.append( pr.getParameterString());
			MoneyFlow moneyFlow = sf.getMoneyFlow();
			if(moneyFlow!=null){
				if(moneyFlow==MoneyFlow.BUY){
					sb.append(" and price>=0 ");
				}else{
					sb.append(" and price<0 ");
				}
			}
			List<BuyRecordStat> list = this.getEntityListWithClassHQL(sb.toString(),pr.getPage(),pr.getPageSize(),BuyRecordStat.class,pr.getParameterValue());
			return list.get(0);
		} catch (BaseException e) {
			throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
					"获取购买记录统计异常", e);
		}
	}

	/**
	 * 获取雷达统计分组中的最大值
	 * @param sf
	 * @return
	 */
	public Long getMaxValueOfBuyRecord(BuyRecordAnalyseStatSearch sf){
		try {
			PageRequest pr = sf.buildQuery();
			StringBuffer sb = new StringBuffer();
			if(sf.getType()==GroupType.COUNT){
                sb.append("select max(vv) from (");
                sb.append("select "+sf.getGroupField()+",count(*) vv from buy_record ");
                sb.append(pr.getParameterString());
                sb.append(" group by "+sf.getGroupField());
                sb.append(") as aa ");
            }else if(sf.getType()==GroupType.TOTALPRICE){
                sb.append("select max(totalPrice) as vv from buy_record ");
                sb.append(pr.getParameterString());
            }else{
                sb.append("select max(shipment) as vv from buy_record ");
                sb.append(pr.getParameterString());
            }
			List list = this.getEntityListNoPageSQL(sb.toString(),pr.getParameterValue());
            if(list.isEmpty()){
            	return 0L;
			}
            Object oo = list.get(0);
			if(sf.getType()==GroupType.COUNT){
				BigInteger vv = (BigInteger) oo;
				return  vv.longValue();
			}else{
				BigDecimal vv = (BigDecimal) oo;
				return vv.longValue();
			}
		} catch (BaseException e) {
			throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
					"获取雷达统计分组中的最大值异常", e);
		}
	}

	/**
	 * 实时的分析雷达统计
	 * @param sf
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BuyRecordRadarStat> getRadarStat(BuyRecordAnalyseStatSearch sf){
		try {
			PageRequest pr = sf.buildQuery();
			String groupField =sf.getGroupField();
			GroupType type = sf.getType();
			StringBuffer sql=new StringBuffer();
			sql.append("select groupId,indexValue,count(*) totalCount,sum(pp) totalPrice from ( ");
			if(groupField.equals("price_region")){
				sql.append("select getPriceRegionId(total_price,"+sf.getUserId()+") as groupId,");
			}else{
				sql.append("select "+groupField+" as groupId,");
			}
			sql.append(MysqlUtil.dateTypeMethod("buy_date",sf.getDateGroupType())+" as indexValue");
			if(type== GroupType.COUNT||type== GroupType.TOTALPRICE ){
				//统计次数
				sql.append(" ,total_price as pp from buy_record ");
			}else if(type== GroupType.SHIPMENT){
				//运费
				sql.append(" ,shipment as pp from buy_record ");
			}
			sql.append(pr.getParameterString());
			if(sf.isUseStatable()){
				sql.append(getStatableCondition());
			}
			sql.append(") as aa ");
			sql.append("group by groupId,indexValue ");
			sql.append("order by indexValue,groupId ");
			List<BuyRecordRadarStat> list =this.getEntityListWithClassSQL(sql.toString(),pr.getPage(),pr.getPageSize(),BuyRecordRadarStat.class,pr.getParameterValue());
			return list;
		} catch (BaseException e) {
			throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
					"获取实时的分析雷达统计异常", e);
		}
	}
	/**
	 * 实时的分析统计
	 * @param sf
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BuyRecordRealTimeStat> getAnalyseStat(BuyRecordAnalyseStatSearch sf){
		try {
			PageRequest pr = sf.buildQuery();
			String groupField =sf.getGroupField();
			GroupType type = sf.getType();
			StringBuffer sql=new StringBuffer();
			if(groupField.equals("price_region")){
				sql.append("select getPriceRegionId(total_price,"+sf.getUserId()+") as priceRegion");
			}else{
				sql.append("select "+groupField);
			}
			if(type== GroupType.COUNT){
				//统计次数
				sql.append(" ,count(*) as cc from buy_record ");
			}else if(type== GroupType.TOTALPRICE){
				//价格
				sql.append(" ,sum(total_price) as cc from buy_record ");
			}else if(type== GroupType.SHIPMENT){
				//运费
				sql.append(" ,sum(shipment) as cc from buy_record ");
			}
			sql.append(pr.getParameterString());
			if(sf.isUseStatable()){
				sql.append(getStatableCondition());
			}
			if(groupField.equals("price_region")){
				sql.append(" group by priceRegion");
			}else{
				sql.append(" group by "+groupField);
			}
			if(sf.getChartType() == ChartType.BAR){
				sql.append(" order by cc desc");
			}
			List<Object[]> list =this.getEntityListNoPageSQL(sql.toString(), pr.getParameterValue());
			List<BuyRecordRealTimeStat> result = new ArrayList<BuyRecordRealTimeStat>();
			for (Object[] oo : list) {
				BuyRecordRealTimeStat bb = new BuyRecordRealTimeStat();
				Object nameFiled=oo[0];
				if(nameFiled==null){
					bb.setName("未知");
				}else{
					Object serierIdObj = oo[0];
					if(serierIdObj==null){
						//防止为NULL
						serierIdObj="0";
					}
					String name = getSerierName(serierIdObj.toString(),groupField);
					bb.setName(name);
				}
				double value =Double.valueOf(oo[1].toString());
				bb.setValue(value);
				result.add(bb);
			}
			return result;
		} catch (BaseException e) {
			throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
					"获取实时统计异常", e);
		}
	}

	/**
	 * 获取统计图表中的系列名称
	 * @param serierIdObj
	 * @param groupField
	 * @return
	 */
	private String getSerierName(String serierIdObj,String groupField){
		try {
			if(groupField.equals("shop_name")){
				return serierIdObj;
			}
			int serierId = Integer.valueOf(serierIdObj);
			if(groupField.equals("buy_type_id")){
                BuyType bt = (BuyType) this.getEntityById(BuyType.class, serierId);
                if(bt==null){
                    return "未知";
                }else{
                    return bt.getName();
                }
            }else if(groupField.equals("goods_type_id")||groupField.equals("sub_goods_type_id")){
                GoodsType gt = (GoodsType) this.getEntityById(GoodsType.class, serierId);
                if(gt==null){
                    return "未知";
                }else{
                    return gt.getName();
                }
            }else if(groupField.equals("price_region")){
				PriceRegion gt = (PriceRegion) this.getEntityById(PriceRegion.class, serierId);
				if(gt==null){
					return "未知";
				}else{
					return gt.getName()+"("+ Constant.MONEY_SYMBOL+gt.getMinPrice()+"~"+Constant.MONEY_SYMBOL+gt.getMaxPrice()+")";
				}
			}else if(groupField.equals("payment")){
				Payment payment = Payment.getPayment(serierId);
				if(payment==null){
					return "未知";
				}else{
					return payment.getName();
				}
			}
			return "未知";
		} catch (BaseException e) {

			return "未知";
		}
	}

	/**
	 * 按时间来统计
	 * @param sf
	 * @return
	 */
	public List<BuyRecordDateStat> statBuyRecordByDate(BuyRecordDateStatSearch sf){
		try {
			PageRequest pr = sf.buildQuery();
			DateGroupType dateGroupType = sf.getDateGroupType();
			BuyRecordPriceType priceType = sf.getPriceType();
			StringBuffer sb = new StringBuffer();
			sb.append("select indexValue,round(sum(price)) as totalPrice ,count(0) as totalCount ");
			sb.append("from (");
			sb.append("select "+ MysqlUtil.dateTypeMethod("buy_date",dateGroupType)+" as indexValue,");
			if(priceType==BuyRecordPriceType.SHIPMENT){
				sb.append("shipment as price");
			}else if(priceType==BuyRecordPriceType.TOTALPRICE){
				sb.append("total_price as price");
			}
			sb.append(" from buy_record ");
			sb.append(pr.getParameterString());
			if(sf.isUseStatable()){
				sb.append(getStatableCondition());
			}
			sb.append(") tt group by indexValue ");
			sb.append(" order by indexValue");
			List<BuyRecordDateStat> list = this.getEntityListWithClassSQL(sb.toString(),pr.getPage(),pr.getPageSize(),BuyRecordDateStat.class,pr.getParameterValue());
			return list;
		} catch (BaseException e) {
			throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
					"按时间来统计异常", e);
		}
	}

	/**
	 * 获取购买记录关键字
	 * @return
	 */
	public List<String> getKeywordsList(BuyRecordKeywordsSearch sf){
		try {
			PageRequest pr = sf.buildQuery();
			String sql="select distinct keywords from buy_record ";
			sql+=pr.getParameterString();
			return this.getEntityListSQL(sql,0,0,pr.getParameterValue());
		} catch (BaseException e) {
			throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
					"获取购买记录关键字异常", e);
		}
	}

	/**
	 * 按时间来统计回家记录
	 * @param sf
	 * @return
	 */
	public List<BackHomeDateStat> statBackHomeByDate(BackHomeDateStatSearch sf){
		try {
			PageRequest pr =sf.buildQuery();
			StringBuffer sb = new StringBuffer();
			sb.append("select indexValue,round(sum(price)) as totalPrice ,count(0) as totalCount ");
			sb.append("from (");
			DateGroupType dateGroupType = sf.getDateGroupType();
			sb.append("select "+ MysqlUtil.dateTypeMethod("buy_date",dateGroupType)+" as indexValue,");
			sb.append("total_price as price");
			sb.append(" from buy_record ");
			sb.append(pr.getParameterString());
			sb.append(") tt group by indexValue ");
			sb.append(" order by indexValue");
			List<BackHomeDateStat> list = this.getEntityListWithClassSQL(sb.toString(),pr.getPage(),pr.getPageSize(),BackHomeDateStat.class,pr.getParameterValue());
			return list;
		} catch (BaseException e) {
			throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
					"按时间来统计回家记录异常", e);
		}
	}

	/**
	 * 按关键字来统计购买记录
	 * @param sf 查询条件
	 * @return
	 */
	public List<BuyRecordKeywordsStat> statBuyRecordByKeywords(BuyRecordKeywordsStatSearch sf){
		try {
			PageRequest pr = sf.buildQuery();
			StringBuffer sb = new StringBuffer();
			sb.append("select keywords,round(sum(price)) as totalPrice ,count(0) as totalCount from buy_record ");
			pr.setNeedWhere(true);
			sb.append(pr.getParameterString());
			if(sf.isUseStatable()){
				sb.append(getStatableCondition());
			}
			//必须要有关键字
			sb.append("and keywords is not null ");
			sb.append("group by keywords ");
			sb.append("order by totalPrice desc");
			List<BuyRecordKeywordsStat> list = this.getEntityListWithClassSQL(sb.toString(),pr.getPage(),pr.getPageSize(),BuyRecordKeywordsStat.class,pr.getParameterValue());
			return list;
		} catch (BaseException e) {
			throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
					"按关键字来统计购买记录异常", e);
		}
	}

	/**
	 * 获取需要统计的条件
	 * @return
	 */
	private String getStatableCondition(){
		StringBuffer sb = new StringBuffer();
		//排除不需要统计的记录
		sb.append(" and goods_type_id in (select id from goods_type where statable =1 and pid=0 ) ");
		//有可能子商品类型为空
		sb.append(" and (sub_goods_type_id in (select id from goods_type where statable =1 and pid>0 ) or sub_goods_type_id is null )");
		sb.append(" and statable != 0 ");
		return sb.toString();
	}

	/**
	 * 获取购买记录
	 * @param startTime
	 * @param endTime
	 * @param userId
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public List<BuyRecord> getBuyRecord(Date startTime, Date endTime, Long userId,int page, int pageSize){
		try {
			String hql="from BuyRecord where buyDate>=? and buyDate<=? and userId=? ";
			return this.getEntityListHQL(hql,page,pageSize,startTime,endTime,userId);
		} catch (BaseException e) {
			throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,
					"获取购买记录异常", e);
		}
	}


}
