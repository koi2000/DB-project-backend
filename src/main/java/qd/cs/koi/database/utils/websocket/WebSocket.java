package qd.cs.koi.database.utils.websocket;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.web.bind.annotation.RestController;

/**
 * WebSocket
 * 
 * @author ZhongLi
 * @date 2020-01-12
 * @version 1.0.0
 */
@ServerEndpoint("/WebSocket/{id}")
@RestController
public class WebSocket {

	// 存储会话
	private static ConcurrentHashMap<String, WebSocket> webSocket = new ConcurrentHashMap<String, WebSocket>();

	private String id;
	private Session session;

	private int number = 0;
	/**
	 * 接入连接回调
	 * 
	 * @param session 会话对象
	 * @param id      会话ID
	 * @throws Exception 异常
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam("id") String id) throws Exception {
		this.id = id;
		this.session = session;
		webSocket.put(id, this);
		// 检验后端能否正常给前端发送信息
		number++;
		sendMessageToId(this.id, "前端你好，我是后端，我正在通过WebSocket给你发送消息"+number);
		System.out.println(id + "接入连接");
	}

	/**
	 * 关闭连接回调
	 */
	@OnClose
	public void onClose() {
		webSocket.remove(this.id);
		System.out.println(this.id + "关闭连接");
	}

	/**
	 * 收到客户端发来消息回调
	 * 
	 * @param message
	 */
	@OnMessage
	public void onMessage(String message) {
		System.out.println(this.id + "发来消息：" + message);
		try {
			System.out.println("发出消息"+message);
			this.sendMessageToAll(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 会话出现错误回调
	 * 
	 * @param error   错误信息
	 */
	@OnError
	public void onError(Throwable error) {

	}

	/**
	 * 发送消息给客户端
	 * 
	 * @param message 消息
	 * @throws IOException 异常
	 */
	public void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}

	/**
	 * 给指定的会话发送消息
	 * 
	 * @param id      会话ID
	 * @param message 消息
	 * @throws IOException 异常
	 */
	public void sendMessageToId(String id, String message) throws IOException {
		webSocket.get(id).sendMessage(message);
	}

	/**
	 * 群发消息
	 * 
	 * @param message 消息
	 * @throws IOException 异常
	 */
	public void sendMessageToAll(String message) throws IOException {
		for (String key : webSocket.keySet()) {
			try {
				webSocket.get(key).sendMessage(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

