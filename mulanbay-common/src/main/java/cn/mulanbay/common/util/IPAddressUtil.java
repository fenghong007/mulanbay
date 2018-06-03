package cn.mulanbay.common.util;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

/**
 * IP地址获取工具类
 *
 * @author fenghong
 * @create 2018-01-20 21:44
 */
public class IPAddressUtil {

	private static Logger logger = Logger.getLogger(IPAddressUtil.class);

	public static String getLocalIpAddress() {
		try {
			String ip = InetAddress.getLocalHost().getHostAddress();
			return ip;
		} catch (Exception e) {
			logger.error("获取本地IP地址异常", e);
			return null;
		}
	}

	/**
	 * 位置信息
	 * 
	 * @return
	 */
	public static String getIpAddressInfo(String ip) {
		InputStream inputStream = null;
		ByteArrayOutputStream out = null;
		try {// 获取HttpURLConnection连接对象
			String urlStr ="http://ip.taobao.com/service/getIpInfo.php";
			if (urlStr == null || urlStr.isEmpty()) {
				logger.warn("系统没有配置IP信息的服务器地址");
				return null;
			}
			// String urlStr ="http://ip.taobao.com/service/getIpInfo.php?ip="
			// + ip;
			URL url = new URL(urlStr + "?ip=" + ip);
			HttpURLConnection httpConn = (HttpURLConnection) url
					.openConnection();
			// 设置连接属性
			httpConn.setConnectTimeout(3000);
			httpConn.setDoInput(true);
			httpConn.setRequestMethod("GET");
			// 获取相应码
			int respCode = httpConn.getResponseCode();
			if (respCode == 200) {
				String jsonStr = "";
				// ByteArrayOutputStream相当于内存输出流
				out = new ByteArrayOutputStream();
				inputStream = httpConn.getInputStream();
				byte[] buffer = new byte[1024];
				int len = 0;

				while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
					out.write(buffer, 0, len);
				}
				// 将内存流转换为字符串
				jsonStr = new String(out.toByteArray());
				return jsonStr;
			}
		} catch (Exception e) {
			logger.error("获取IP地址信息异常", e);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				logger.error("关闭流异常", e);
			}
		}
		return null;
	}
	
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

}
