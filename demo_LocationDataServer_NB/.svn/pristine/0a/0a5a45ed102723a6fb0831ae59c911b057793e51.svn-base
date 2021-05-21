package com.bn.geo;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import com.bn.geo.data.LocationData;
/**
* <p>Title: 博能位置数据服务器 - Publisher</p>
*
* <p>Description:
* 	位置数据服务器的MQ生产者对象，负责连接MQ服务器，并且发送对应位置数据到对应的主题上
*   每次在new Publisher时，需传入MQ服务器地址和主题名称
* </p>
*
* <p>Copyright: Copyright bnkj(c) 2019</p>
*
* <p>Company: 北京博能科技股份有限公司</p>
*
* @author jiangjie
* @version 1.0
*/
public class Publisher {
	/**
	 * log4j日志输出对象
	 */
	private static Logger log4j = Logger.getLogger(Publisher.class);
	/**
	 * MQ服务器连接地址
	 */
	private static String brokerURL;  
	/**
	 *  ConnectionFactory ：连接工厂，JMS用它创建连接
	 */
	private static ConnectionFactory factory;  
	/**
	 *  Connection ：JMS客户端到JMS Provider的连接
	 */
    private static Connection connection;  
    /**
     *  Session：一个发送或接收消息的线程
     */
    private  Session session;  
    /**
     *  Destination ：消息的目的地;消息发送给谁.
     */
    private Destination destination;  
    /**
     *  MessageProducer：消息发送者
     */
    private MessageProducer producer;  
    /**
     * 要发送到消息主题topic的名称
     */
    private  String topicName;
    /**
     * Connection 是否连接成功
     */
    private boolean isConncted = false;   
	
    
	/**
	 * @return the brokerURL
	 */
	public String getBrokerURL() {
		return brokerURL;
	}
	/**
	 * @param brokerURL the brokerURL to set
	 */
	public void setBrokerURL(String brokerURL) {
		this.brokerURL = brokerURL;
	}
	/**
	 * @return the topicName
	 */
	public String getTopicName() {
		return topicName;
	}
	/**
	 * @param topicName the topicName to set
	 */
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	/**
	 * @return the isConncted
	 */
	public boolean isConncted() {
		return isConncted;
	}
	/**
	 * @param isConncted the isConncted to set
	 */
	public void setConncted(boolean isConncted) {
		this.isConncted = isConncted;
	}
	/**
	 * @param brokerURL is MQ url,topicName is MQ Topic
	 */
	public Publisher(String brokerURL,String topicName)
	{
		this.brokerURL=brokerURL;
		this.topicName=topicName;
	}

	/**
	 * 启动MQ连接服务器，并且设置相关参数
	 */
	public void start(){
	    try{
			 //构造ConnectionFactory实例对象，此处采用ActiveMq的实现
	       factory = new ActiveMQConnectionFactory(
	       		 				ActiveMQConnection.DEFAULT_USER,
	       		                ActiveMQConnection.DEFAULT_PASSWORD,
	       		               	brokerURL);  
	      //创建connection对象
	       connection = factory.createConnection();  
	       //创建session对象
	       session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);  
	       //根据主题创建destination对象
	       destination = session.createTopic(topicName); 
	       //创建生产者对象
	       producer = session.createProducer(destination);  
	       // 消息不持久化
	       producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		   // 消息存活时间：60s
	       producer.setTimeToLive(60000);
	       //设置完了后，才连接 
	       connection.start(); 
	       //更改是否连接参数的值为true
	       isConncted = true;
	       
		}
		catch(Exception ex){
			System.out.println("启动发布者失败!");
			//更改是否连接参数的值为false
			isConncted = false;
		}
    }
	/**
	 * 连接MQ服务器失败后，则关闭MQ会话和连接
	 */
	public void close()  {  
    	try{
	        if (session != null) {  
	        	session.close();  
	        } 
	        if (connection != null) {  
	        	connection.stop();
	            connection.close();  
	        } 
	        //更改是否连接参数的值为false
	        isConncted = false; 
    	}
    	catch( JMSException jex){
			System.out.println("停止发布者失败！");
    	}
    }  
      
   /**
    * 将位置数据对象信息发送到MQ对应的主题上
    * @param data：位置数据对象LocationData
    */
	public void sendMessage(LocationData data)  { 
    	try{
    		//确认MQ是否连接，若没有，则重新连接
    		if(!isConncted){
    			start();
    		}	
    		//将data数据封装成ObjectMessage对象
    		ObjectMessage objm=this.session.createObjectMessage(data);
    		//MQ生产者对象将ObjectMessage对象数据发送到对应的MQ主题上
    		producer.send(objm);
    		//调试时，输出正常发送的位置数据信息
			log4j.debug("位置数据已发送到（"+topicName+"）主题："+data.toString()+"\n");
    	}catch(Exception ex){
    		System.out.println("ActiveMQConnection连接失败,请确认！");
    		//关闭连接和session会话
    		close();
    		//更改是否连接参数的值为false
    		isConncted = false;
    	}
    }	
}
