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
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
@ServerEndpoint("/websocket/{userId}")  // 接口路径 ws://localhost:8084/websocket/userId;
public class WebSocket {

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    private String userId;

    //concurrent包的线程安全Map，用来存放每个客户端对应的WebSocket对象。
    private static ConcurrentHashMap<String,WebSocket> webSockets = new ConcurrentHashMap<>();

    // 使用线程安全的原子整数来存在线连接数
    private static AtomicInteger onlineCount = new AtomicInteger(0);

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
            this.userId = userId;
            this.session = session;
            webSockets.put(userId,this);
            onlineCount.incrementAndGet();
            log.info("【websocket消息】有新的连接，当前连接总数为:"+onlineCount.get());
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
            webSockets.remove(this.userId);
            onlineCount.decrementAndGet();
            log.info("【websocket消息】连接断开，当前连接总数为:"+onlineCount.get());
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
        WebSocket webSocket = webSockets.get(message.getReceiver());
        if (webSocket != null && webSocket.session.isOpen()) {
            try {
                log.info("【websocket消息】 单点消息:"+message);
                webSocket.session.getAsyncRemote().sendText(mapper.writeValueAsString(message));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            log.info("消息接收者还未建立WebSocket连接");
        }
        if (message.getContent() != null) {
            template.opsForHash().put(message.getReceiver(),message.getContent().getId(),message);
        }
    }

    //拉取未读消息
    public void pullUnreadMessage(String userId){
        List<Message> list = template.opsForHash().values(userId);
        if (list != null && !list.isEmpty()) {
            for (Message message : list) {
                sendOneMessage(message);
            }
            log.info("拉取未读消息成功");
        } else {
            log.info("暂无未读消息");
        }
    }

    //处理收到的消息
    public void messageHandler(String message) throws JsonProcessingException {
        //收到此消息不做任何处理
        if (message.equals("1")) {
            return;
        }
        //反序列化消息
        Message value = mapper.readValue(message, Message.class);
        //若消息类型为notice则进行消息转发处理
        if (value.getType().equals("notice")) {
            //生成初始交换记录
            ExchangeRecord record = exchangeService.add(value.getContent());
            //将交换记录回设，作为消息内容发送
            value.setContent(record);
            sendOneMessage(value);
        }
        //若消息类型为agree则进行物品交换处理
        if (value.getType().equals("agree")) {
            //开始交换
            boolean exchange = exchangeService.exchange(value.getContent());

            Message message1 = new Message();
            message1.setReceiver(value.getSender());
            if (exchange) {
                //交换成功后更新交换记录状态为成功，并向前端发送success消息
                value.getContent().setStatus(ExchangeStatus.EXCHANGE_SUCCESS);
                message1.setType("success");
            } else {
                //交换失败则更新交换记录状态为失败，并向前端发送error消息
                value.getContent().setStatus(ExchangeStatus.EXCHANGE_FAILED);
                message1.setType("error");
            }
            sendOneMessage(message1);
            exchangeService.update(value.getContent());
            //删除已处理的消息
            template.opsForHash().delete(value.getSender(),value.getContent().getId());
        }
        //若消息类型为reject则更新交换记录状态为失败
        if (value.getType().equals("reject")) {
            value.getContent().setStatus(ExchangeStatus.EXCHANGE_FAILED);
            exchangeService.update(value.getContent());

            //删除已处理的消息
            template.opsForHash().delete(value.getSender(),value.getContent().getId());
        }


    }
}

