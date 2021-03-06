package com.bn.geo;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.bn.geo.data.LocationData;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
* <p>Title: 博能位置数据服务器 - LocationDataServerHandler</p>
*
* <p>Description:
* 	位置数据服务器的接收数据的主要处理类
* </p>
*
* <p>Copyright: Copyright bnkj(c) 2018</p>
*
* <p>Company: 北京博能科技股份有限公司</p>
*
* @author william
* @version 1.0
*/
public class LocationDataServerHandler extends SimpleChannelInboundHandler<String> {
	/**
	 * log4j日志输出对象
	 */
	private static Logger log4j = Logger.getLogger(LocationDataServerUdpHandler.class);
	
	/**
	 * 用于数据处理的线程池对象
	 */
	private static ExecutorService threadPool = null;
	
	/**
	 * topic分发管理器
	 */
	private static LocationTopicAssign topicAssign = null;
	
	private static boolean back = false;
			
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
		// 每条数据以回车换行符进行分隔，最后一行数据必须是空行，否则会漏掉一条数据
		// 建议的位置数据格式如下：
		// 位置数据类型,设备编号,时间,x,y,z,速度,方向,wkid
		// 位置数据类型：01-航空器，02-车辆，03-施工人员,04-无动力设备,05-旅客。。。。。。
		// 时间：yyyy-mm-dd hh:mm:ss，包含时分秒
		// x、y、z、速度、方向都是必须填写的内容，如果没有也必须填写0占位，否则将会在转换的时候抛出异常，从而忽略掉这条数据
		// wkid：空间参考id，如：4326 自定义的空间参考会约定一个编号
		// 收到数据后会进行如下处理：
		// 1、构造LocationData对象
		// 2、连接AMQ(建议AMQ只连接一次，不用每次都重新连接)
		// 3、将所有数据发布到LOCATION.ALL这个topic中
		// 4、将航空器的数据发布到LOCATION.AIRCRAFT这个topic中
		// 5、将车辆的位置数据发布到LOCATION.VEHICLE这个topic中，其它类型的数据如法炮制，分别发送到各自对应的topic中
		// 通过调用LocationTopicAssign类中的run方法来进行
		
		//接收到数据，向客户端反馈1(字符串)
		ctx.writeAndFlush("1");
		
		//后续的处理过程
		log4j.debug("接收到原始数据TCP：" + s);			
		// 创建一个线程池，指定最大的线程数，如果线程数超过最大数量，就进行排队，但是一旦达到最大数量，线程是不会释放的
		if(threadPool == null) threadPool = Executors.newFixedThreadPool(Config.MAX_PROCESS_THREAD);
		//在线程中进行数据处理
		threadPool.execute(()->{
			try {
				//log4j.info("当前线程：" + Thread.currentThread().getName());				
				//将字符串转化成LocationData对象
				LocationData data = LocationData.parse(s);
				
				if(topicAssign == null){
					topicAssign = new LocationTopicAssign();
				}	
				Date date = new Date();
				if(back) {
					back = false;
					data.setLocationTime(date);
					data.setSpeed(40);
				}else {
					back = true;
					data.setSpeed(0);
					data.setLocationTime(new Date(date.getTime() - 10*1000));
				}
				
				topicAssign.assign(data);;

			}
			catch(Exception _ex) {
				_ex.printStackTrace();
			}
		});
			
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		
		log4j.info("Client:" + incoming.remoteAddress() + "在线");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		
		log4j.info("Client:" + incoming.remoteAddress() + "掉线");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		Channel incoming = ctx.channel();
		log4j.info("Client:" + incoming.remoteAddress() + "异常");
		
		// 当出现异常就关闭连接
		cause.printStackTrace();
		ctx.close();
	}
}