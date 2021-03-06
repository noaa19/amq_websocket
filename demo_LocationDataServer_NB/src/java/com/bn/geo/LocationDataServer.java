package com.bn.geo;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
* <p>Title: 博能位置数据服务器 - LocationDataServer</p>
*
* <p>Description:
* 	位置数据服务器的主要功能是接收文本格式的位置数据，并转换成LocationData对象，
*	根据数据的类型(航空器、车辆、人员等)分别将数据分发到不同的AMQ的Topic中。
*	服务器采用Netty来实现，通过tcp协议接收传入的位置数据字串
* </p>
*
* <p>Copyright: Copyright bnkj(c) 2018</p>
*
* <p>Company: 北京博能科技股份有限公司</p>
*
* @author william
* @version 1.0
*/
public class LocationDataServer {
	/**
	 * 服务器监听的端口
	 */
	private int port;
	
	/**
	 * 构造函数
	 * @param port 服务器监听的端口
	 */
	public LocationDataServer(int port) {
		this.port = port;
	}
	
	/**
	 * 启动位置数据服务器
	 * @throws Exception
	 */
	public void startTcpService() throws Exception {

		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new LocationDataServerInitializer())
				.option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true);
			System.out.println("LocationDataServer 启动了");
			// 绑定端口，开始接收进来的连接
			ChannelFuture f = b.bind(port).sync();

			// 等待服务器 socket 关闭 。
			// 在这个例子中，这不会发生，但你可以优雅地关闭你的服务器。
			f.channel().closeFuture().sync();
		} 
		finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();

			System.out.println("LocationDataServer 关闭了");
		}
	}
		
	/**
	 * 启动UDP服务
	 */
	public void startUdpService() {
        try {
            Bootstrap b = new Bootstrap();
            EventLoopGroup group = new NioEventLoopGroup();
            b.group(group)
	            .channel(NioDatagramChannel.class)
	            .option(ChannelOption.SO_BROADCAST, true)
	            .handler(new LocationDataServerUdpHandler());
            
            b.bind(port).sync().channel().closeFuture().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
	
	/**
	 * 启动位置数据服务器，包含TCP和UDP服务
	 */
	public void start() throws Exception {
		//开启一个子线程，启动UDP服务
		new Thread(()->{
			this.startUdpService();
		}).start();

		// 主线程中启动TCP服务
		this.startTcpService();		
	}
	
	/**
	 * 服务器入口函数
	 * @param args
	 * 		传入的参数，程序接受一个参数，在启动的时候可以指定监听的端口，默认端口是9901
	 * @throws Exception
	 */
	public static void main(String[] args) {
		int port;
		
		
		
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} 
		else {
			port = 9902;
		}
						
		//加载配置信息到Config中相应的静态变量中
		Config.load();
		
		try {
			
			LocationDataServer server = new LocationDataServer(port);
			server.start();
		}
		catch(Exception _ex) {
			// 出现异常，退出系统
			System.exit(-1);
			
			_ex.printStackTrace();
		}
	}
}