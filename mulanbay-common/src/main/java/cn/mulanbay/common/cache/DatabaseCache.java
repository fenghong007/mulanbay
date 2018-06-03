package cn.mulanbay.common.cache;

/**
 * 数据库缓存
 *
 * @author fenghong
 * @create 2018-01-20 21:44
 */
public interface DatabaseCache {

	public boolean set(String key, String module, Object value, int timeout);

	public Object get(String key, String module);

}
