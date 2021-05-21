package com.bn.geo;

import java.util.Set;

import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.advisory.DestinationSource;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.log4j.Logger;

/**
* <p>Title: 博能位置数据服务器 - AMQUtil</p>
*
* <p>Description:
* 	对AMQ操作的一些封装，包含连接、断开、查询所有topic、发布消息等
* </p>
*
* <p>Copyright: Copyright bnkj(c) 2018</p>
*
* <p>Company: 北京博能科技股份有限公司</p>
*
* @author william
* @version 1.0
*/
public class AMQClientUtil {
	/**
	 * log4j日志输出对象
	 */
	private static Logger log4j = Logger.getLogger(AMQClientUtil.class);

	/**
	 * 连接工厂
	 */
	private static ActiveMQConnectionFactory connectionFactory = null;
	
	/**
	 * AMQ连接
	 */
	public static ActiveMQConnection connection = null;
	
	public static Session session = null;  
	
	/**
	 * 创建AMQ连接工厂，并通过连接工厂创建一个AMQ连接
	 * @throws JMSException
	 */
	public static void connect() throws JMSException {

		//判断与AMQ的连接是否可用,如果不可用将connection设置成null，以便重新连接
		if(connection != null && connection.isStarted())
			return;
		else {
			if(connection != null) {
				connection.close();
				connection = null;
			}
		}
		
		log4j.info("正在尝试与AMQ建立连接...");
		
		// 实例化连接工厂
		if(connectionFactory == null) {
			connectionFactory = new ActiveMQConnectionFactory(Config.AMQ_USERNAME, Config.AMQ_PASSWORD, Config.AMQ_BROKEURL);
			connectionFactory.setTrustAllPackages(true);
		}
		
		//通过连接工厂获取连接
		if(connection == null) {
	        connection = (ActiveMQConnection)connectionFactory.createConnection();
	        connection.addTransportListener(new AMQTranportListener());
	        //启动连接
	        connection.start();
		}
	}
	
	/**
	 * 判断是否与AMQ服务器保持连接
	 * @return
	 */
	public static boolean isConnect() {
		return connection != null && connection.isStarted();
	}
	
	/**
	 * 断开AMQ连接
	 * @throws JMSException
	 */
	public static void disconnect() throws JMSException {
		if(connection != null) {
			connection.close();
			connection = null;
		}
	}
	
	/**
	 * 创建会话
	 * @return 会话对象
	 * @throws JMSException
	 */
	public static Session createSession() throws JMSException {
		//Session session = null;
		if(session ==null){
			if(connection != null && connection.isStarted()){
				session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
			}
		}		
		return session;
	}
	
	/**
	 * 关闭会话
	 * @throws JMSException
	 */
	public static void closeSession(Session session) throws JMSException {
		if(session != null) {
			session.close();
			session = null;
		}
	}

	/**
	 * 获取AMQ中所有的topic
	 * @return Set<ActiveMQTopic>对象
	 * @throws JMSException
	 */
	public static Set<ActiveMQTopic> getTopics() throws JMSException {
		if(connection != null) {
			DestinationSource destinationSource = connection.getDestinationSource();
			
			return destinationSource.getTopics(); 
		}
		else
			return null;
	}
	
	/**
	 * 获取AMQ中所有的Queue
	 * @return Set<ActiveMQQueue>对象
	 * @throws JMSException
	 */
	public static Set<ActiveMQQueue> getQueues() throws JMSException {
		if(connection != null) {
			DestinationSource destinationSource = connection.getDestinationSource();
			
			return destinationSource.getQueues(); 
		}
		else
			return null;
	}
}
