package com.bn.geo;

import org.apache.log4j.Logger;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
* <p>Title: 博能位置数据服务器 - LocationDataServerInitializer</p>
*
* <p>Description:
* 	在netty启动的时候注册解码器、处理器
* </p>
*
* <p>Copyright: Copyright bnkj(c) 2018</p>
*
* <p>Company: 北京博能科技股份有限公司</p>
*
* @author william
* @version 1.0
*/
public class LocationDataServerInitializer extends ChannelInitializer<SocketChannel> {
	/**
	 * log4j日志输出对象
	 */
	private Logger log4j = Logger.getLogger(this.getClass());

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

		pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
		pipeline.addLast("decoder", new StringDecoder());
		pipeline.addLast("encoder", new StringEncoder());
		pipeline.addLast("handler", new LocationDataServerHandler());

		log4j.info("Client:" + ch.remoteAddress() + "连接上");
	}
}