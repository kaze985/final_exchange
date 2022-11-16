package com.lppnb.convertor;

import com.lppnb.domain.model.ExchangeRecord;
import com.lppnb.domain.model.ExchangeStatus;
import com.lppnb.domain.model.Item;
import com.lppnb.dto.data.ExchangeRecordDTO;
import com.lppnb.gatewayimpl.database.dataobject.ExchangeRecordDO;
import org.springframework.beans.BeanUtils;


public class ExchangeRecordConvertor {
    public static ExchangeRecord toEntity(ExchangeRecordDTO exchangeRecordDTO){
        ExchangeRecord exchangeRecord = new ExchangeRecord();
        BeanUtils.copyProperties(exchangeRecordDTO, exchangeRecord);

        Item activePartyItem = new Item();
        Item passivePartyItem = new Item();
        activePartyItem.setId(exchangeRecordDTO.getActivePartyItem());
        activePartyItem.setName(exchangeRecordDTO.getActivePartyItemName());
        activePartyItem.setOwnerId(exchangeRecordDTO.getActiveParty());
        activePartyItem.setOwnerName(exchangeRecordDTO.getActivePartyName());

        passivePartyItem.setId(exchangeRecordDTO.getPassivePartyItem());
        passivePartyItem.setName(exchangeRecordDTO.getPassivePartyItemName());
        passivePartyItem.setOwnerId(exchangeRecordDTO.getPassiveParty());
        passivePartyItem.setOwnerName(exchangeRecordDTO.getPassivePartyName());

        exchangeRecord.setActivePartyItem(activePartyItem);
        exchangeRecord.setPassivePartyItem(passivePartyItem);
        exchangeRecord.setStatus(ExchangeStatus.valueOf(exchangeRecordDTO.getStatus()));

        return exchangeRecord;
    }

    public static ExchangeRecordDO toDataObject(ExchangeRecord record){
        ExchangeRecordDO exchangeRecordDO = new ExchangeRecordDO();
        BeanUtils.copyProperties(record,exchangeRecordDO);

        Item activePartyItem = record.getActivePartyItem();
        Item passivePartyItem = record.getPassivePartyItem();

        if (activePartyItem != null && passivePartyItem != null) {
            exchangeRecordDO.setActiveParty(activePartyItem.getOwnerId());
            exchangeRecordDO.setActivePartyName(activePartyItem.getOwnerName());
            exchangeRecordDO.setActivePartyItem(activePartyItem.getId());
            exchangeRecordDO.setActivePartyItemName(activePartyItem.getName());

            exchangeRecordDO.setPassiveParty(passivePartyItem.getOwnerId());
            exchangeRecordDO.setPassivePartyName(passivePartyItem.getOwnerName());
            exchangeRecordDO.setPassivePartyItem(passivePartyItem.getId());
            exchangeRecordDO.setPassivePartyItemName(passivePartyItem.getName());
        }

        exchangeRecordDO.setStatus(record.getStatus().getStatusName());

        return exchangeRecordDO;
    }
}
