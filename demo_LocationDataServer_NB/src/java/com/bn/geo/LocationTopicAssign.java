package com.bn.geo;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;

import org.apache.log4j.Logger;

import com.bn.geo.data.LocationData;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;

/**
* <p>Title: 博能位置数据服务器 - LocationTopicAssign</p>
*
* <p>Description:
* 	根据LocationData.locationType，将数据发布到不同的AMQ的topic中，任何数据都要发送到LOCATION_ALL中
* </p>
*
* <p>Copyright: Copyright bnkj(c) 2018</p>
*
* <p>Company: 北京博能科技股份有限公司</p>
*
* @author william
* @version 1.0
*/
public class LocationTopicAssign {
	/**
	 * log4j日志输出对象
	 */
	private static Logger log4j = Logger.getLogger(LocationTopicAssign.class);
	
	/**
	 * AMQ中所有位置消息默认的topic，即所有消息都会发布到这个topic中
	 */
	private static String DEFAULT_TOPIC = "00";

	/**
	 * 各种不同的位置类型编号和topic名字对应关系
	 */
	private static Map<String, String> topicMap = null;
	
	/**
	 * 构造函数。读取AMQ、定位类型和topic对应关系等的配置
	 */
	public LocationTopicAssign() {
		if(topicMap == null) {
			topicMap = new HashMap<String, String>();
			
			// 所有的位置数据都发布到这里
			topicMap.put(DEFAULT_TOPIC, "scence.all");
			// 飞行器位置数据
			topicMap.put("01", "location.aircraft");
			// 车辆位置数据
			//topicMap.put("02", "location.vehicle");
			topicMap.put("02", "location.vehicle");
			// 车辆报警数据
			topicMap.put("03", "location.alarm");
			// 无动力设施位置数据
			topicMap.put("04", "location.faci");
			// 旅客位置数据
			topicMap.put("05", "location.pass");
			// 人员定位
		    topicMap.put("06", "location.person");
		    // 人员定位
		    topicMap.put("08", "location.terminal");
		    
		}
	}
	
	/**
	 * 将LocationData发布到对应AMQ的topic中
	 * @param data LocationData对象
	 * @throws JMSException 
	 */
	public void assign(LocationData data) throws JMSException {
		//id存储的是车牌号码或者是sim卡号
		String vehicleNo = data.getId().trim();		
		if(vehicleNo!=null && !"".equals(vehicleNo)){ 
			String topicName = "The-Test-Of-Exchange-Data";
		    //String topicName = topicMap.get(data.getLocationType());		
			//如果是车辆数据或者人员数据，需要将接收到的位置数据按照目标空间参考进行坐标转换
			if("02".equals(data.getLocationType()) || "08".equals(data.getLocationType())) {
				if("08".equals(data.getLocationType())) {
					System.out.println(data);
				}
				//原始数据
//				Point location = new Point(data.getX(), data.getY(), SpatialReference.create(4326));
				//目标空间参考中的数据
//				Point dstLocation = (Point)GeometryEngine.project(location, Config.dstSR);				
				//将转换完毕的数据回写到data中，wkid设置成0，表示已经是目标空间参考
				data.setX(data.getX());
				data.setY(data.getY());			
			}
			//获取生产者对象，将数据发送到MQ			
			PublisherUtil.getPush_publisher(topicName).sendMessage(data);
		}	    
	}
}
