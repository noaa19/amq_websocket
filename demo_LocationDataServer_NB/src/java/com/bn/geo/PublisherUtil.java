package com.bn.geo;

import java.util.HashMap;
import java.util.Map;
/**
* <p>Title: 博能位置数据服务器 - PublisherUtil</p>
*
* <p>Description:
* 	根据主题名称，获取对应主题的生产者（Publisher）对象的工具类
* </p>
*
* <p>Copyright: Copyright bnkj(c) 2019</p>
*
* <p>Company: 北京博能科技股份有限公司</p>
*
* @author jiangjie
* @version 1.0
*/
public class PublisherUtil {
	/**
	 * 推送消息发布者
	 */
	public static Map<String,Publisher> publisherMap;
	
	/**
	 * ActiveMQ服务器连接地址
	 */
	public static String ACTIVEMQ_BROKER_URL = Config.AMQ_BROKEURL;
	
	/**
	 * 根据主题，获取对应主题的生产者对象。若没有对应主题的生产者对象，则新增一个对应主题的生产者（Publisher）对象。
	 * 并且以主题为key,生产者对象为value,放入到publisherMap集合。
	 * 
	 * @return publisher
	 */
	public static Publisher getPush_publisher(String topicName) {
		if(publisherMap == null) {
			//生产者集合对象不存在时，则创建集合对象
			publisherMap=new HashMap<String,Publisher>();
		}
		if (publisherMap.get(topicName) == null) {
			//指定主题的生产者不存在时，则创建对应的生产者，并启动连接。
			Publisher push_publisher = new Publisher(ACTIVEMQ_BROKER_URL, topicName);
			push_publisher.start();
			//将指定主题创建好的生产者对象放入publisherMap集合。
			publisherMap.put(topicName, push_publisher);
		}
		//返回指定主题的的生产者对象
		return publisherMap.get(topicName);
	}
}
