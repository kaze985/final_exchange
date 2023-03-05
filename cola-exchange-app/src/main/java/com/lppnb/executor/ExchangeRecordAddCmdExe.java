package com.lppnb.executor;

import cn.hutool.core.util.IdUtil;
import com.alibaba.cola.dto.SingleResponse;
import com.lppnb.convertor.ExchangeRecordConvertor;
import com.lppnb.domain.gateway.ExchangeGateway;
import com.lppnb.domain.model.ExchangeStatus;
import com.lppnb.dto.ExchangeRecordAddCmd;
import com.lppnb.dto.data.ExchangeRecordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ExchangeRecordAddCmdExe {

    @Autowired
    private ExchangeGateway exchangeGateway;

    public SingleResponse<ExchangeRecordDTO> execute(ExchangeRecordAddCmd cmd) {
        ExchangeRecordDTO recordDTO = cmd.getExchangeRecordDTO();
        recordDTO.setId(IdUtil.getSnowflake(3, 1).nextIdStr());
        recordDTO.setStatus(ExchangeStatus.EXCHANGING.getStatusName());
        recordDTO.setGmtCreated(LocalDateTime.now());
        recordDTO.setGmtModified(LocalDateTime.now());
        exchangeGateway.add(ExchangeRecordConvertor.toEntity(recordDTO));
        return SingleResponse.of(recordDTO);
    }
}
