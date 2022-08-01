package com.geekgame.demo.model;

import lombok.Data;

import java.io.Serializable;
@Data
public class Message implements Serializable {
    private String type;
    private String sender;
    private String receiver;
    private ExchangeRecord content;
}
