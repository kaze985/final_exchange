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

    /**
     * 新增交换记录
     * @param record
     * @return
     */
    @Override
    public ExchangeRecord add(ExchangeRecord record) {
        record.setId(String.valueOf(generator.nextId()));
        record.setStatus(ExchangeStatus.EXCHANGING);
        record.setGmtCreated(LocalDateTime.now());
        record.setGmtModified(LocalDateTime.now());

        int add = recordDAO.add(new ExchangeRecordDO(record));
        if (add == 0){
            return null;
        }
        return record;
    }

    /**
     * 更新交换记录
     * @param record
     * @return
     */
    @Override
    public ExchangeRecord update(ExchangeRecord record) {
        int update = recordDAO.update(new ExchangeRecordDO(record));
        if (update == 0) {
            return null;
        }
        return record;
    }


    /**
     * 交换
     * @param record
     * @return
     */
    @Override
    @GlobalTransactional(rollbackFor = Exception.class, timeoutMills = 300000)
    public boolean exchange(ExchangeRecord record) {
        Item activePartyAtNow = itemService.findById(record.getActivePartyItem().getId());
        Item passivePartyAtNow = itemService.findById(record.getPassivePartyItem().getId());
        Item activeParty = record.getActivePartyItem();
        Item passiveParty = record.getPassivePartyItem();

        //确保两个交换的物品的拥有者都没有改变
        if (!activePartyAtNow.getOwnerId().equals(activeParty.getOwnerId())) {
            return false;
        }
        if (!passivePartyAtNow.getOwnerId().equals(passiveParty.getOwnerId())) {
            return false;
        }

        //开始交换
        String tempId = activeParty.getOwnerId();
        String tempName = activeParty.getOwnerName();
        activeParty.setOwnerId(passiveParty.getOwnerId());
        activeParty.setOwnerName(passiveParty.getOwnerName());
        passiveParty.setOwnerId(tempId);
        passiveParty.setOwnerName(tempName);
        itemService.update(activeParty);
        itemService.update(passiveParty);
        return true;
    }
}
