import java.net.InetSocketAddress;

import org.apache.log4j.Logger;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

public class SendUDPPacket {
	public static void main(String args[]) {
		Logger log4j = Logger.getLogger(SendUDPPacket.class);
		 
		EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .handler(new SimpleChannelInboundHandler<DatagramPacket>() {

						@Override
						protected void channelRead0(ChannelHandlerContext context, DatagramPacket packet) throws Exception {
							ByteBuf buf = packet.copy().content();
						    byte[] req = new byte[buf.readableBytes()];
						    buf.readBytes(req);
						    String s = new String(req, "UTF-8");
					        
						    // 打印收到的信息
						    log4j.info("接收到响应：" + ("1".equals(s) ? "发送成功" : "发送失败"));							
						}
                    	
                    });

            Channel ch = b.bind(0).sync().channel();

            while(true) {
	            ch.writeAndFlush(new DatagramPacket(
	                    Unpooled.copiedBuffer("01,ADSB001,2018-05-24 11:06:20,116.01234567,30.1236789,0,0,0,4326,1", CharsetUtil.UTF_8),
	                    new InetSocketAddress("192.168.0.151", 9901))).sync();
	            
	            Thread.sleep(5000);
            }
        }
        catch(Exception _ex) {
        	_ex.printStackTrace();
        }
	}
}
