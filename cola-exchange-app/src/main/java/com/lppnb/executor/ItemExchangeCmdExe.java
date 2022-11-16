package com.lppnb.executor;

import com.alibaba.cola.dto.Response;
import com.lppnb.domain.gateway.ItemGateway;
import com.lppnb.domain.model.Item;
import com.lppnb.dto.ItemExchangeCmd;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemExchangeCmdExe {

    @Autowired
    private ItemGateway itemGateway;

    @GlobalTransactional(rollbackFor = Exception.class, timeoutMills = 300000)
    public Response execute(ItemExchangeCmd cmd) {
        Item activePartyAtNow = itemGateway.getItem(cmd.getExchangeRecordDTO().getActivePartyItem()).getData();
        Item passivePartyAtNow = itemGateway.getItem(cmd.getExchangeRecordDTO().getPassivePartyItem()).getData();
        
        //确保两个交换的物品的拥有者都没有改变
        if (!activePartyAtNow.getOwnerId().equals(cmd.getExchangeRecordDTO().getActiveParty())) {
            return Response.buildFailure("666", "物品拥有者已经改变");
        }
        if (!passivePartyAtNow.getOwnerId().equals(cmd.getExchangeRecordDTO().getPassiveParty())) {
            return Response.buildFailure("666", "物品拥有者已经改变");
        }

        //开始交换
        String tempId = activePartyAtNow.getOwnerId();
        String tempName = activePartyAtNow.getOwnerName();
        activePartyAtNow.setOwnerId(passivePartyAtNow.getOwnerId());
        activePartyAtNow.setOwnerName(passivePartyAtNow.getOwnerName());
        passivePartyAtNow.setOwnerId(tempId);
        passivePartyAtNow.setOwnerName(tempName);
        itemGateway.updateItem(activePartyAtNow);
        itemGateway.updateItem(passivePartyAtNow);

        return Response.buildSuccess();
    }
}
