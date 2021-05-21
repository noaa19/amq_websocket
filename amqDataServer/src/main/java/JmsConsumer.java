import org.junit.Test;


import org.apache.activemq.ActiveMQConnectionFactory;
 
import javax.jms.*;
import java.io.IOException;
 
public class JmsConsumer {
    public static final String ACTIVEMQ_URL = "tcp://localhost:61616";
    public static final String QUEUE_NAME = "The-Test-Of-Exchange-Data";
 
    public static void main(String[] args) throws JMSException, IOException {
        // 创建连接工厂，按照给定的url地址采用默认的用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        // 通过连接工厂，获取Connection并启动
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        // 创建Session
        // 有两个参数，第一个是事务，第二个是签收，后面详细介绍
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 创建目的地（目的地有两个子接口，分别是Queue和Topic）
        Destination des = session.createTopic(QUEUE_NAME);
        // 创建消费者，指明从queue取消息
        MessageConsumer messageConsumer = session.createConsumer(des);
        // 通过监听器的方式来消费消息
        messageConsumer.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
                System.out.println(message);
            }
        });
        System.in.read();// 用于阻塞住线程，让消费者消费完成才能关闭
        // 按照资源打开的相反顺序关闭资源
        messageConsumer.close();
        session.close();
        connection.close();
    }
}
