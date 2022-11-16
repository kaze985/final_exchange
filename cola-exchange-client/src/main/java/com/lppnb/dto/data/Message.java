package com.lppnb.dto.data;

import lombok.Data;

import java.io.Serializable;

/**
 * 消息类
 */
@Data
public class Message implements Serializable {
    /**
     * 消息类型
     */
    private String type;

    /**
     * 消息发送者
     */
    private String sender;

    /**
     * 消息接收者
     */
    private String receiver;

    /**
     * 消息内容
     */
    private ExchangeRecordDTO content;
}
