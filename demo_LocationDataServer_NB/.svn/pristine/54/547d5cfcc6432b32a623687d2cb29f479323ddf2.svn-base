package com.bn.geo;

import java.io.IOException;

import org.apache.activemq.transport.TransportListener;
import org.apache.log4j.Logger;

public class AMQTranportListener implements TransportListener {
	/**
	 * log4j日志输出对象
	 */
	private Logger log4j = Logger.getLogger(this.getClass());

	@Override
	public void onCommand(Object arg0) {
		
	}

	@Override
	public void onException(IOException arg0) {
		
	}

	@Override
	public void transportInterupted() {
		//与AMQ中断
		log4j.info("与AMQ服务器中断通信");
	}

	@Override
	public void transportResumed() {
		//与AMQ恢复通信
		log4j.info("与AMQ服务器恢复通信");
	}

}
