import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import com.bn.geo.AMQClientUtil;
import com.bn.geo.Config;
import com.bn.geo.data.LocationData;

/**
* <p>Title: 博能位置数据服务器 - LoctionDataConsumer</p>
*
* <p>Description:
* 	地理位置数据消费者测试，这个demo主要从AMQ中读取
* </p>
*
* <p>Copyright: Copyright bnkj(c) 2018</p>
*
* <p>Company: 北京博能科技股份有限公司</p>
*
* @author william
* @version 1.0
*/
public class LoctionDataConsumer {
	public static void main(String args[]) {
		// 连接到AMQ
			//加载配置，有些参数会在连接AMQ的时候用到
			Config.load();
			
			while(true) {
				try {
					Thread.sleep(3000);
				
					//如果保持连接就什么都不做，断开连接则重新连一次
					if(AMQClientUtil.isConnect()) continue;
					
					AMQClientUtil.connect();
		
					Session session = AMQClientUtil.createSession();
					
					Destination destination = session.createTopic("location.vehicle");
					MessageConsumer messageConsumer = session.createConsumer(destination);
					
					// 设置接收监听器
					messageConsumer.setMessageListener(new MessageListener() {
		                public void onMessage(Message message) {
		                    if (message instanceof ObjectMessage) {
		                        try {
		                        	ObjectMessage msg = (ObjectMessage) message;
		                        	// 接收到的LocationData数据
		                        	LocationData data = (LocationData)msg.getObject();
		                        	
		                        	System.out.println("Received:" + data.toString());
		                        	
		                            msg.acknowledge();
		                        } catch (Exception e) {
		                            e.printStackTrace();
		                        }
		                    }
		
		                }
		            });
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	}
}
