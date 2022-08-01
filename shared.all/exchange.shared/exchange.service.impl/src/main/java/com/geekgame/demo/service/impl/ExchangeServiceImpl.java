package com.geekgame.demo.service.impl;

import com.geekgame.demo.dao.ExchangeRecordDAO;
import com.geekgame.demo.dataobject.ExchangeRecordDO;
import com.geekgame.demo.model.ExchangeRecord;
import com.geekgame.demo.model.ExchangeStatus;
import com.geekgame.demo.model.Item;
import com.geekgame.demo.service.ExchangeService;
import com.geekgame.demo.service.ItemService;
import com.geekgame.demo.util.SnowflakeIdGenerator;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
public class ExchangeServiceImpl implements ExchangeService {

    @Autowired
    private ExchangeRecordDAO recordDAO;
    @Autowired
    private SnowflakeIdGenerator generator;
    @DubboReference(timeout = 300000, retries = 0)
    private ItemService itemService;

    @Override
    public ExchangeRecord add(ExchangeRecord record) {
        record.setId(String.valueOf(generator.nextId()));
        record.setStatus(ExchangeStatus.EXCHANGING);
        record.setGmtCreated(LocalDateTime.now());
        record.setGmtModified(LocalDateTime.now());

        Item activePartyItem = itemService.findById(record.getActivePartyItem());
        Item passivePartyItem = itemService.findById(record.getPassivePartyItem());
        record.setActivePartyName(activePartyItem.getOwnerName());
        record.setActivePartyItemName(activePartyItem.getName());
        record.setPassivePartyName(passivePartyItem.getOwnerName());
        record.setPassivePartyItemName(passivePartyItem.getName());

        int add = recordDAO.add(new ExchangeRecordDO(record));
        if (add == 0){
            return null;
        }
        return record;
    }

    @Override
    public ExchangeRecord update(ExchangeRecord record) {
        int update = recordDAO.update(new ExchangeRecordDO(record));
        if (update == 0) {
            return null;
        }
        return record;
    }



    @Override
    @GlobalTransactional(rollbackFor = Exception.class, timeoutMills = 300000)
    public ExchangeRecord exchange(ExchangeRecord record) {
        Item activeParty = itemService.findById(record.getActivePartyItem());
        Item passiveParty = itemService.findById(record.getPassivePartyItem());
        String tempId = activeParty.getOwnerId();
        String tempName = activeParty.getOwnerName();
        activeParty.setOwnerId(passiveParty.getOwnerId());
        activeParty.setOwnerName(passiveParty.getOwnerName());
        passiveParty.setOwnerId(tempId);
        passiveParty.setOwnerName(tempName);
        itemService.update(activeParty);
        itemService.update(passiveParty);
        return record;
    }
}
