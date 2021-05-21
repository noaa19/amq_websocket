package com.bn.geo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.activemq.ActiveMQConnection;

import com.esri.arcgisruntime.geometry.SpatialReference;

/**
* <p>Title: 博能位置数据服务器 - Config</p>
*
* <p>Description:
* 	集中存储系统参数，在启动程序的时候，默认参数会从配置表中进行加载
* </p>
*
* <p>Copyright: Copyright bnkj(c) 2018</p>
*
* <p>Company: 北京博能科技股份有限公司</p>
*
* @author william
* @version 1.0
*/
public class Config {
	/**
	 * 最大数据处理线程，用于指定在LocationServer中接收到了文本后进行后续处理的线程数
	 */
	public static int MAX_PROCESS_THREAD = 10;
	
	/**
	 * AMQ的的连接地址
	 */
	public static String AMQ_BROKEURL = ActiveMQConnection.DEFAULT_BROKER_URL;
	
	/**
	 * AMQ用户名
	 */
	public static String AMQ_USERNAME = ActiveMQConnection.DEFAULT_USER;
	
	/**
	 * AMQ密码
	 */
	public static String AMQ_PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
	
	/**
	 * 目标空间参考
	 */
	public static SpatialReference dstSR = null;
	
	/**
	 * 从配置文件中读取配置参数并写入到对应的变量中
	 */
	public static void load() {
		// 定义Properties对象，并从配置文件中读取配置信息
		InputStream in = null;

		Properties properties = new Properties();
		try {
			in = Config.class.getClassLoader().getResourceAsStream("config.properties");
			properties.load(in);
			
			//提取出maxProcessThread参数
			String sMaxProcessThread = properties.getProperty("maxProcessThread");
			MAX_PROCESS_THREAD = Integer.parseInt(sMaxProcessThread);
			
			//设置AMQ连接地址
			String sAMQBrokeUrl = properties.getProperty("amq.brokeurl");
			if(sAMQBrokeUrl != null && !"".equals(sAMQBrokeUrl))
				AMQ_BROKEURL = sAMQBrokeUrl;
			
			//设置AMQ用户名和密码
			String sAMQUserName = properties.getProperty("amq.userName");
			if(sAMQUserName != null && !"".equals(sAMQUserName))
				AMQ_USERNAME = sAMQUserName;
			
			String sAMQPassword = properties.getProperty("amq.password");
			if(sAMQPassword != null && !"".equals(sAMQPassword))
				AMQ_PASSWORD = sAMQPassword;
			
			//车辆的目标空间参考：根据dst.wkid和dst.wkt生成位置数据的目标空间参考
			//如果wkid没有配置，以wkt为准；如果两者都没有配置，则默认4326
			int wkid = 0;
			String sWkid = properties.getProperty("vehicle.wkid");
			if(sWkid != null && !"".equals(sWkid)) {
				wkid = Integer.parseInt(sWkid);
			}
			
			String wkt = null;
			String sWkt = properties.getProperty("vehicle.wkt");
			if(sWkt != null && !"".equals(sWkt)) {
				wkt = sWkt;
			}
			
			//如果wkid传入的是一个非0的数字，则用这个数字构造目标空间参考
			//如果传入的是0，还需要判断wkt是否有效，如果有wkt，则以wkt作为空间参考，否则用默认的4326
			if(wkid == 0) {
				//如果传入的是0，需要看看wkt是否传入，如果传入则用wkt构造目标空间参考；如果wkt也没有传入则沟通默认的4326空间参考
				if(wkt == null)
					dstSR = SpatialReference.create(4326);
				else
					dstSR = SpatialReference.create(wkt);
			}
			else {
				dstSR = SpatialReference.create(wkid);
			}

		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(in != null) in.close();
			} 
			catch (IOException e) {
			}
		}
	}
}
