package cn.mulanbay.persistent.service;

import cn.mulanbay.common.exception.ErrorCode;
import cn.mulanbay.common.exception.PersistentException;
import cn.mulanbay.persistent.common.BaseException;
import cn.mulanbay.persistent.dao.BaseHibernateDao;
import cn.mulanbay.persistent.query.PageRequest;
import cn.mulanbay.persistent.query.PageResult;
import cn.mulanbay.persistent.query.Sort;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 基础service，封装常用的操作方法
 *
 * @author fenghong
 * @create 2018-01-20 21:44
 */
@Service
@Transactional
public class BaseService extends BaseHibernateDao {

	private static Logger logger = Logger.getLogger(BaseService.class);

	/**
	 * 删除对象
	 * 
	 * @param object
	 *            object
	 */
	public void deleteObject(Object object) {
		try {
			this.removeEntity(object);
		} catch (BaseException e) {
			throw new PersistentException(ErrorCode.OBJECT_DELETE_ERROR,"删除对象失败！",e);
		}
	}

	/**
	 * 增加对象
	 * 
	 * @param object
	 *            object
	 */
	public void saveObject(Object object) {
		try {
			this.saveEntity(object);
		} catch (BaseException e) {
			throw new PersistentException(ErrorCode.OBJECT_ADD_ERROR,"增加对象失败！",e);
		}
	}

	/**
	 * 增加对象
	 * 
	 * @param objects
	 *            object
	 */
	@SuppressWarnings("rawtypes")
	public void saveObjects(List objects) {
		try {
			this.saveEntitys(objects.toArray());
		} catch (BaseException e) {
			throw new PersistentException(ErrorCode.OBJECT_ADD_ERROR,"增加多个对象失败！",e);
		}
	}

	/**
	 * 更新对象
	 * 
	 * @param object
	 *            object
	 */
	public void updateObject(Object object) {
		try {
			this.updateEntity(object);
		} catch (BaseException e) {
			throw new PersistentException(ErrorCode.OBJECT_UPDATE_ERROR,"更新对象失败！",e);
		}
	}
	
	/**
	 * 更新对象
	 * 
	 * @param object
	 *            object
	 */
	public void updateByMergeObject(Object object) {
		try {
			this.mergeEntity(object);
		} catch (BaseException e) {
			throw new PersistentException(ErrorCode.OBJECT_UPDATE_ERROR,"更新对象失败！",e);
		}
	}
	
	
	/**
	 * 动态更新一个对象，需要该类打@DynamicUpdate
	 * 
	 * @param object
	 * @param cls
	 * @param id
	 */
	public void updateObjectDynamic(Object object, Class<?> cls, Serializable id) {
		try {
			Object dbo = this.getObject(cls, id);
			if (dbo == null) {
				throw new PersistentException(
						ErrorCode.OBJECT_GET_ERROR, "获取对象["
								+ cls.getSimpleName() + "],id[" + id + "]失败！");
			} else {
				BeanUtils.copyProperties(object, dbo);
				this.updateEntity(dbo);
			}
		} catch (BaseException e) {
			throw new PersistentException(
					ErrorCode.OBJECT_UPDATE_ERROR, "更新对象失败！", e);
		}
	}

	/**
	 * 获取对象
	 * 
	 * @param c
	 *            c
	 * @param id
	 *            id
	 * @return Object Object
	 */
	//@Async
	@SuppressWarnings("unchecked")
	public <T> T getObject(Class<T> c, Serializable id) {
		try {
			return (T) this.getEntityById(c, id);
		} catch (BaseException e) {
			throw new PersistentException(ErrorCode.OBJECT_GET_ERROR,"获取对象["+c.getSimpleName()+"],id["+id+"]失败！",e);
		}
	}

	/**
	 * 获取对象
	 *
	 * @param beanName
	 *
	 * @param id
	 *            id
	 * @return Object Object
	 */
	//@Async
	@SuppressWarnings("unchecked")
	public Object getObject(String beanName, Serializable id,String idField) {
		try {
			String hql="from "+beanName+" where "+idField+"=?";
			return this.getEntityForOne(hql,id);
		} catch (BaseException e) {
			throw new PersistentException(ErrorCode.OBJECT_GET_ERROR,"获取对象["+beanName+"],id["+id+"]失败！",e);
		}
	}

	/**
	 * 获取对象
	 *
	 * @param id
	 *            id
	 * @param userId
	 *
	 * @return Object Object
	 */
	//@Async
	@SuppressWarnings("unchecked")
	public <T> T getObjectWithUser(Class<T> c, Serializable id,Long userId) {
		try {
			String hql="from "+c.getSimpleName()+" where id=? and userId=? ";
			return (T) this.getEntityForOne(hql,id,userId);
		} catch (BaseException e) {
			throw new PersistentException(ErrorCode.OBJECT_GET_ERROR,"获取对象["+c.getSimpleName()+"],id["+id+"],userId["+userId+"]失败！",e);
		}
	}


	/**
	 * 删除对象
	 * 
	 * @param c
	 *            c
	 * @param fieldName
	 *            fieldName
	 * @param id
	 *            id
	 */
	@SuppressWarnings("rawtypes")
	public void deleteObject(Class c, String fieldName, Serializable id) {
		try {
			String hql = "delete from " + c.getSimpleName() + " t where t."
					+ fieldName + "=?";
			this.updateEntitys(hql, id);
		} catch (BaseException e) {
			throw new PersistentException(ErrorCode.OBJECT_DELETE_ERROR,"删除对象失败！",e);
		}
	}
	

	/**
	 * 删除对象
	 * 
	 * @param c
	 *            c
	 * @param id
	 *            id
	 */
	@SuppressWarnings("rawtypes")
	public void deleteObject(Class c, Serializable id) {
		try {
			this.removeEntity(c, id);
		} catch (Exception e) {
			throw new PersistentException(ErrorCode.OBJECT_DELETE_ERROR,"删除对象失败！",e);
		}
	}

	/**
	 * 批量删除对象
	 *
	 * @param c
	 *            c
	 * @param ids
	 *            ids
	 */
	@SuppressWarnings("rawtypes")
	public void deleteObjects(Class c, Serializable[] ids) {
		try {
			for(Serializable id:ids){
				this.removeEntity(c, id);
			}
		} catch (Exception e) {
			throw new PersistentException(ErrorCode.OBJECT_DELETE_ERROR,"删除对象失败！",e);
		}
	}

	/**
	 * 删除对象
	 * @param c
	 * @param ids
	 * @param userId
	 */
	public void deleteObjectsWithUser(Class c, Serializable[] ids,Long userId) {
		try {
			for(Serializable id:ids){
				String hql="delete from "+c.getSimpleName()+" where id=? and userId=? ";
				this.updateEntitys(hql,id,userId);
			}
		} catch (Exception e) {
			throw new PersistentException(ErrorCode.OBJECT_DELETE_ERROR,"删除对象失败！",e);
		}
	}


	/**
	 * 批量删除对象
	 *
	 * @param c
	 *            c
	 * @param ids
	 *            ids
	 */
	@SuppressWarnings("rawtypes")
	public void deleteObjects(Class c, Collection<Serializable> ids) {
		try {
			for(Serializable id:ids){
				this.removeEntity(c, id);
			}
		} catch (Exception e) {
			throw new PersistentException(ErrorCode.OBJECT_DELETE_ERROR,"删除对象失败！",e);
		}
	}


	public void saveOrUpdateObject(Object object) {
		try {
			this.saveAndUpdateEntity(object);
		} catch (BaseException e) {
			e.printStackTrace();
			throw new PersistentException(ErrorCode.OBJECT_ADD_ERROR,"增加或更新对象失败！",e);
		}

	}

	@SuppressWarnings("unchecked")
	public <T> PageResult<T> getBeanResult(Class<T> c, int page, int pageSize, Sort sort) {
		try {
			PageResult<T> qb = new PageResult<T>();
			StringBuffer hql = new StringBuffer();
			hql.append("from " + c.getName());
			if (page >= 0) {
				long maxRow = this
						.getCount("select count(*) " + hql.toString());
				qb.setMaxRow(maxRow);
			}
			if (sort != null) {
				hql.append(" order by " + sort.getSortString());
			}
			List<T> list = this.getEntityListHQL(hql.toString(), page,
					pageSize);
			qb.setBeanList(list);
			return qb;
		} catch (BaseException e) {
			throw new PersistentException(ErrorCode.OBJECT_GET_ERROR,"获取列表异常", e);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getBeanList(Class<T> c, int page, int pageSize, Sort sort) {
		try {
			StringBuffer hql = new StringBuffer();
			hql.append("from " + c.getName());
			if (sort != null) {
				hql.append(" order by " + sort.getSortString());
			}
			List<T> list = this.getEntityListHQL(hql.toString(), page,
					pageSize);
			return list;
		} catch (BaseException e) {
			throw new PersistentException(ErrorCode.OBJECT_GET_ERROR,"获取列表异常", e);
		}
	}

	//
	@SuppressWarnings("unchecked")
	public <T> PageResult<T> getBeanResult(PageRequest pr) {
		try {
			PageResult<T> qb = new PageResult<T>();
			String hql = "from " + pr.getBeanClass().getName();
			hql += pr.getParameterString();
			Object[] values = pr.getParameterValue();
			logger.debug("查询语句 :"+ hql);
			if (pr.getPage() >= 0) {
				logger.debug("getCount查询语句 :"+"select count(*) " + hql);
				long maxRow = this.getCount("select count(*) " + hql,
						values);
				qb.setMaxRow(maxRow);
			}
			hql+=pr.getSortString();
			List<T> list = this.getEntityListHQL(hql.toString(),
					pr.getPage(), pr.getPageSize(), values);
			qb.setBeanList(list);
			return qb;
		} catch (BaseException e) {
			throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,"获取列表异常", e);
		}
	}

	//
	@SuppressWarnings("unchecked")
	public <T> List<T> getBeanList(PageRequest pr) {
		try {
			String hql = "from " + pr.getBeanClass().getName();
			hql += pr.getParameterString();
			Object[] values = pr.getParameterValue();
			hql+=pr.getSortString();
			List<T> list = this.getEntityListHQL(hql.toString(),
					pr.getPage(), pr.getPageSize(), values);
			return list;
		} catch (BaseException e) {
			throw new PersistentException(ErrorCode.OBJECT_GET_LIST_ERROR,"获取列表异常", e);
		}
	}

	/**
	 * 执行通用的JOB存储过程异常(非查询类)
	 * 
	 * @param procedureName
	 * @param objects
	 */
	@SuppressWarnings("unused")
	public void updateJobProcedure(String procedureName, Object... objects) {
		try {
			String sql = "";
			if (objects == null || objects.length == 0) {
				sql = "{call " + procedureName + "()}";
			} else {
				String para = "";
				for (Object o : objects) {
					para += "?,";
				}
				para = para.substring(0, para.length() - 1);
				sql = "{call " + procedureName + "(" + para + ")}";
			}
			this.execSqlUpdate(sql, objects);
		} catch (BaseException e) {
			throw new PersistentException("执行通用的JOB存储过程异常", e);
		}
	}
}
