package com.bn.geo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.bn.geo.data.LocationData;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.ReferenceCountUtil;

/**
* <p>Title: 博能位置数据服务器 - LocationDataServerUdpHandler</p>
*
* <p>Description:
* 	位置数据服务器的接收数据的主要处理类，这个类主要是对UDP协议接收到的数据进行处理
* </p>
*
* <p>Copyright: Copyright bnkj(c) 2018</p>
*
* <p>Company: 北京博能科技股份有限公司</p>
*
* @author william
* @version 1.0
*/
public class LocationDataServerUdpHandler extends SimpleChannelInboundHandler<DatagramPacket> {
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

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
		ByteBuf buf = packet.copy().content();
	    byte[] req = new byte[buf.readableBytes()];
	    buf.readBytes(req);
	    String s = new String(req, "UTF-8");
        
	    // 打印收到的信息
	    log4j.debug("接收到原始数据UDP：" + s);

        // 向客户端发送消息，响应1
        byte[] bytes = "1".getBytes("UTF-8");
        DatagramPacket respPcket = new DatagramPacket(Unpooled.copiedBuffer(bytes), packet.sender());
        // 向客户端发送消息
        ctx.writeAndFlush(respPcket);	
        
		// 创建一个线程池，指定最大的线程数，如果线程数超过最大数量，就进行排队，但是一旦达到最大数量，线程是不会释放的
		if(threadPool == null) threadPool = Executors.newFixedThreadPool(Config.MAX_PROCESS_THREAD);
		//在线程中进行数据处理
		threadPool.execute(()->{
			try {
				//log4j.info("当前线程：" + Thread.currentThread().getName());				
				//将字符串转化成LocationData对象
				LocationData data = LocationData.parse(s);
				log4j.debug("locationData对象数组：" + data);
				if(topicAssign == null){
					topicAssign = new LocationTopicAssign();
				}				
				topicAssign.assign(data);
			}
			catch(Exception _ex) {
				_ex.printStackTrace();
			}finally {
				ReferenceCountUtil.release(buf);			
			}
		});
     }
}