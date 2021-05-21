package com.bn.geo;
import java.io.File;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class consumer {
//	 private static final String ACTIVEMQ_URL = "tcp://localhost:61616";
//
//	    public static void run() throws JMSException {
//	        // 创建连接工厂
//	        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
//	        // 创建连接
//	        Connection connection = activeMQConnectionFactory.createConnection();
//	        // 打开连接
//	        connection.start();
//	        // 创建会话
//	        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//	        // 创建队列目标,并标识队列名称，消费者根据队列名称接收数据
//	        Destination destination = session.createTopic("The-Test-Of-Exchange-Data");
//	        // 创建消费者
//	        MessageConsumer consumer = session.createConsumer(destination);
//	        // 创建消费的监听
//	        consumer.setMessageListener(new MessageListener() {
//	            public void onMessage(Message message) {
//	                System.out.println("消费的消息：" + message);
//	            }
//	        });
//	    }

}
