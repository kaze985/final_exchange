package com.lppnb.executor;

import com.alibaba.cola.dto.Response;
import com.lppnb.convertor.ExchangeRecordConvertor;
import com.lppnb.domain.gateway.ExchangeGateway;
import com.lppnb.dto.ExchangeRecordUpdateCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExchangeRecordUpdateCmdExe {

    @Autowired
    private ExchangeGateway exchangeGateway;

    public Response execute(ExchangeRecordUpdateCmd cmd) {
        exchangeGateway.update(ExchangeRecordConvertor.toEntity(cmd.getExchangeRecordDTO()));
        return Response.buildSuccess();
    }
}
