package com.lppnb.domain.model;

import com.alibaba.cola.domain.Entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 交换记录
 */
@Data
@Entity
public class ExchangeRecord {
    /**
     * 交换记录id
     */
    private String id;

    /**
     * 主动方物品信息
     */
    private Item activePartyItem;

    /**
     * 被动方物品信息
     */
    private Item passivePartyItem;

    /**
     * 交换记录的状态
     */
    private ExchangeStatus status;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;

}
