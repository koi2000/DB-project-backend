package qd.cs.koi.database.websocket.websocket;


import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import qd.cs.koi.database.websocket.entity.Message;
import qd.cs.koi.database.websocket.utils.MessageUtil;
import qd.cs.koi.database.websocket.utils.RedisUtil;
import qd.cs.koi.database.websocket.utils.StrUtil;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


//@ServerEndpoint(value = "/websocket")
//@Component
public class ApplicationWebSocket {
    @Autowired
    public RedisUtil redisUtil;
    //用户记录当前在线人数
    private static final AtomicInteger onlineCount = new AtomicInteger(0);
    //存储在线人数 key 存储用户名 value 存储每个客户端对应的websocket
    private static final Map<String, ApplicationWebSocket> webSocket = new ConcurrentHashMap<>();
    private Session session;
    private String sendName;

    /**
     * 当有新连接加入时触发
     */
    @OnOpen
    public void onOpen(Session session) {
        addOnlineCount();           //在线数加1
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
        this.session = session;
        //重新把聊天列表推送给客户端
        sendMessNames();
    }

    /**
     * 当客户端给服务端发送消息时候触发
     * @param text json字符串
     */
    @OnMessage
    public void onMessage(String text) {
        try {
            StrUtil.emptyStr(text);
            ObjectMapper mapper = new ObjectMapper();
            Message message = mapper.readValue(text, Message.class);
            StrUtil.emptyStr(message.getSendName());
            message.setCreateDate(DateUtil.now());
            System.out.println(message);
            String type = message.getType();
            Method method = this.getClass().getDeclaredMethod(type, Message.class);
            method.invoke(this, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 客户端关闭的时候触发
     */
    @OnClose
    public void onClose() {
        System.out.println(sendName + "退出了聊天室!");
        StrUtil.emptyStr(sendName);
        ApplicationWebSocket.webSocket.remove(sendName);
        subOnlineCount();
        sendMessNames();
    }

    /**
     * 发生错误的时候触发
     */
    @OnError
    public void onError(Throwable throwable){
        System.out.println("发生错误");
        throwable.printStackTrace();
    }

    /**
     * 返回所有用户名
     */
    public Set<String> getNames() {
        return webSocket.keySet();
    }

    public void sendMessageAll(String jsonMessage)  {
        try {
            Set<String> names = getNames();
            for (String name : names) {
                ApplicationWebSocket.webSocket.get(name).session.getBasicRemote().sendText(jsonMessage);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 给所有客户端推送消息
     */
    public void sendMessageAll( Message message) {
        MessageUtil.emptyMessage(message);
        message.setText(message.getText().replaceAll("\n", "<br>"));
        String jsonMessage = JSON.toJSONString(message);
        sendMessageAll(jsonMessage);
    }

    /**
     * 单独给一位客户端推送消息
     */
    public void sendMessage(Message message) throws IOException {
        MessageUtil.emptyMessage(message);
        message.setText(message.getText().replaceAll("\n", "<br>"));
        String jsonMessage = JSON.toJSONString(message);
        ApplicationWebSocket.webSocket.get(message.getReceiveName()).session.getBasicRemote().sendText(jsonMessage);
    }

    /**
     * 机器人
     */
    public void sendRobot(Message message) throws IOException {
        MessageUtil.emptyMessage(message);
        final String URL = "http://api.qingyunke.com/api.php?key=free&appid=0&msg=" + message.getText();
        message.setUrl("dist/images/robot.png");
        String result = MessageUtil.sendGetRequest(URL);
        result = result.substring(23, result.length() -2);
        result =  result.replaceAll("\\{br}","<br>");
        String toName = message.getSendName();
        message.setSendName("机器人");
        message.setText(result);
        String jsonMessage = JSON.toJSONString(message);
        ApplicationWebSocket.webSocket.get(toName).session.getBasicRemote().sendText(jsonMessage);
    }

    /**
     * 设置客户端name 和实例化 websocket对象
     */
    public void setting(Message message) {
        MessageUtil.emptyMessage(message);
        StrUtil.emptyStr(message.getSendName());
        this.sendName = message.getSendName();
        ApplicationWebSocket.webSocket.put(this.sendName, this);
        sendMessNames();
    }

    /**
     * 推送所有客户端 在线的用户
     */
    public void sendMessNames(){
        String json = MessageUtil.getMessNames(getNames());
        sendMessageAll(json);
    }
    public static synchronized int getOnlineCount() {
        return onlineCount.get();
    }

    public static synchronized void addOnlineCount() {
        ApplicationWebSocket.onlineCount.getAndIncrement();
    }

    public static synchronized void subOnlineCount() {
        ApplicationWebSocket.onlineCount.getAndDecrement();
    }
}
