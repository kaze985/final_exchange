package com.geekgame.demo.websocket;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geekgame.demo.model.ExchangeRecord;
import com.geekgame.demo.model.ExchangeStatus;
import com.geekgame.demo.model.Message;
import com.geekgame.demo.service.ExchangeService;
import com.geekgame.demo.service.impl.ExchangeServiceImpl;
import com.geekgame.demo.util.ApplicationContextProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@Slf4j
@ServerEndpoint("/websocket/{userId}")  // 接口路径 ws://localhost:8084/webSocket/userId;
public class WebSocket {

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocket> webSockets =new CopyOnWriteArraySet<>();

    // 用来存在线连接数
    private static Map<String,Session> sessionPool = new HashMap<String,Session>();

    //因为@ServerEndpoint不支持注入，所以使用SpringUtils获取IOC实例
    private RedisTemplate template = (RedisTemplate) ApplicationContextProvider.getBean("redisTemplateInit");

    private ObjectMapper mapper = ApplicationContextProvider.getBean(ObjectMapper.class);

    private ExchangeService exchangeService = ApplicationContextProvider.getBean(ExchangeServiceImpl.class);

    /**
     * 链接成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value="userId") String userId) {
        try {
            this.session = session;
            webSockets.add(this);
            sessionPool.put(userId, session);
            log.info("【websocket消息】有新的连接，总数为:"+webSockets.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        pullUnreadMessage(userId);
    }

    /**
     * 链接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        try {
            webSockets.remove(this);
            log.info("【websocket消息】连接断开，总数为:"+webSockets.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message
     * @param
     */
    @OnMessage
    public void onMessage(String message) throws JsonProcessingException {
        log.info("【websocket消息】收到客户端消息:"+message);
        messageHandler(message);
    }

    /** 发送错误时的处理
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误,原因:"+error.getMessage());
        error.printStackTrace();
    }

    // 发送单点消息
    public void sendOneMessage(Message message) {
        Session session = sessionPool.get(message.getReceiver());
        if (session != null&&session.isOpen()) {
            try {
                log.info("【websocket消息】 单点消息:"+message);
                session.getAsyncRemote().sendText(mapper.writeValueAsString(message));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            log.info("消息接收者还未建立WebSocket连接，发送的消息将被存储到Redis的列表中");
            template.opsForList().rightPush(message.getReceiver(),message);
        }
    }

    //拉取未读消息
    public void pullUnreadMessage(String userId){
        List<Message> list = template.opsForList().range(userId, 0, -1);
        if (list != null && !list.isEmpty()) {
            for (Message message : list) {
                sendOneMessage(message);
            }
            template.delete(userId);
            log.info("拉取未读消息成功");
        } else {
            log.info("暂无未读消息");
        }
    }

    //处理收到的消息
    public void messageHandler(String message) throws JsonProcessingException {
        if (message.equals("1")) {
            return;
        }
        Message value = mapper.readValue(message, Message.class);
        if (value.getType().equals("notice")) {
            ExchangeRecord record = exchangeService.add(value.getContent());
            value.setContent(record);
            sendOneMessage(value);
        }
        if (value.getType().equals("agree")) {
            exchangeService.exchange(value.getContent());

            value.getContent().setStatus(ExchangeStatus.EXCHANGE_SUCCESS);
            exchangeService.update(value.getContent());
        }
        if (value.getType().equals("reject")) {
            value.getContent().setStatus(ExchangeStatus.EXCHANGE_FAILED);
            exchangeService.update(value.getContent());
        }

    }
}

