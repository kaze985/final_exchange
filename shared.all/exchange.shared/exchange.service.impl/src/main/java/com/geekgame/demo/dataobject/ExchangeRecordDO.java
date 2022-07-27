package com.geekgame.demo.dataobject;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.geekgame.demo.model.ExchangeRecord;
import com.geekgame.demo.model.ExchangeStatus;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
public class ExchangeRecordDO implements Serializable {
    private String id;

    private String activeParty;

    private String passiveParty;

    private String activePartyItem;

    private String passivePartyItem;

    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreated;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModified;

    public ExchangeRecordDO() {
    }
    public ExchangeRecordDO(ExchangeRecord record){
        BeanUtils.copyProperties(record,this);
        this.status=record.getStatus().getStatusName();
    }

    public ExchangeRecord toModel(){
        ExchangeRecord record = new ExchangeRecord();
        BeanUtils.copyProperties(this,record);
        record.setStatus(ExchangeStatus.valueOf(this.status));
        return record;
    }
}
