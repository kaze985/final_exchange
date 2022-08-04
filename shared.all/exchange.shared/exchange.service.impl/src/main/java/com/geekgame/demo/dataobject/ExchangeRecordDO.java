package com.geekgame.demo.dataobject;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.geekgame.demo.model.ExchangeRecord;
import com.geekgame.demo.model.ExchangeStatus;
import com.geekgame.demo.model.Item;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
public class ExchangeRecordDO implements Serializable {
    private String id;

    private String activeParty;

    private String activePartyName;

    private String passiveParty;

    private String passivePartyName;

    private String activePartyItem;

    private String activePartyItemName;

    private String passivePartyItem;

    private String passivePartyItemName;

    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreated;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModified;

    public ExchangeRecordDO() {
    }
    public ExchangeRecordDO(ExchangeRecord record){
        BeanUtils.copyProperties(record,this);
        Item activePartyItem = record.getActivePartyItem();
        Item passivePartyItem = record.getPassivePartyItem();

        if (activePartyItem !=null && passivePartyItem != null) {
            this.activeParty= activePartyItem.getOwnerId();
            this.activePartyName= activePartyItem.getOwnerName();
            this.activePartyItem= activePartyItem.getId();
            this.activePartyItemName= activePartyItem.getName();

            this.passiveParty= passivePartyItem.getOwnerId();
            this.passivePartyName= passivePartyItem.getOwnerName();
            this.passivePartyItem= passivePartyItem.getId();
            this.passivePartyItemName= passivePartyItem.getName();
        }

        this.status=record.getStatus().getStatusName();
    }

    public ExchangeRecord toModel(){
        ExchangeRecord record = new ExchangeRecord();
        BeanUtils.copyProperties(this,record);

        Item activePartyItem = new Item();
        Item passivePartyItem = new Item();
        
        activePartyItem.setId(this.activePartyItem);
        activePartyItem.setName(this.activePartyItemName);
        activePartyItem.setOwnerId(this.activeParty);
        activePartyItem.setOwnerName(this.activePartyName);

        passivePartyItem.setId(this.passivePartyItem);
        passivePartyItem.setName(this.passivePartyItemName);
        passivePartyItem.setOwnerId(this.passiveParty);
        passivePartyItem.setOwnerName(this.passivePartyName);

        record.setActivePartyItem(activePartyItem);
        record.setPassivePartyItem(passivePartyItem);
        record.setStatus(ExchangeStatus.valueOf(this.status));
        return record;
    }
}
