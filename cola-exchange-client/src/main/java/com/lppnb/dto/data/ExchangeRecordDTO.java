package com.lppnb.dto.data;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExchangeRecordDTO {
    /**
     * 交换记录id
     */
    private String id;

    private String activeParty;

    private String activePartyName;

    private String passiveParty;

    private String passivePartyName;

    private String activePartyItem;

    private String activePartyItemName;

    private String passivePartyItem;

    private String passivePartyItemName;

    /**
     * 交换记录的状态
     */
    private String status;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;
}
