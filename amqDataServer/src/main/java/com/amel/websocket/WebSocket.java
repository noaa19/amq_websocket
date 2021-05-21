package com.amel.websocket;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocketServer")
public class WebSocket {

	private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<WebSocket>();
	private Session session;

	@OnOpen
	public void onopen(Session session) throws EncodeException {
		this.session = session;
		webSocketSet.add(this);
		System.out.println("WebSocket开启连接......");
	}

	@OnClose
	public void onclose(Session session) {
		System.out.println("WebSocket关闭连接......");

	}

	@OnMessage
	public void onsend(Session session, String msg) {
		try {
			session.getBasicRemote().sendText("client" + session.getId() + "say:" + msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized static void sendMsg() throws Exception {
		for(WebSocket WS:webSocketSet) {
			WS.session.getBasicRemote().sendText("从amq收到数据...");
		}
		
	}

}