package com.lppnb.service;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import com.lppnb.api.ExchangeService;
import com.lppnb.dto.ExchangeRecordAddCmd;
import com.lppnb.dto.ExchangeRecordUpdateCmd;
import com.lppnb.dto.ItemExchangeCmd;
import com.lppnb.dto.data.ExchangeRecordDTO;
import com.lppnb.executor.ExchangeRecordAddCmdExe;
import com.lppnb.executor.ExchangeRecordUpdateCmdExe;
import com.lppnb.executor.ItemExchangeCmdExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExchangeServiceImpl implements ExchangeService {

    @Autowired
    private ItemExchangeCmdExe itemExchangeCmdExe;

    @Autowired
    private ExchangeRecordAddCmdExe exchangeRecordAddCmdExe;

    @Autowired
    private ExchangeRecordUpdateCmdExe exchangeRecordUpdateCmdExe;


    @Override
    public SingleResponse<ExchangeRecordDTO> addExchangeRecord(ExchangeRecordAddCmd cmd) {
        return exchangeRecordAddCmdExe.execute(cmd);
    }

    @Override
    public Response updateExchangeRecord(ExchangeRecordUpdateCmd cmd) {
        return exchangeRecordUpdateCmdExe.execute(cmd);
    }

    @Override
    public Response exchangeItem(ItemExchangeCmd cmd) {
        return itemExchangeCmdExe.execute(cmd);
    }
}
