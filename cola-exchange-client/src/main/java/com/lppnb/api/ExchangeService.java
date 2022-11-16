package com.lppnb.api;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import com.lppnb.dto.ExchangeRecordAddCmd;
import com.lppnb.dto.ExchangeRecordUpdateCmd;
import com.lppnb.dto.ItemExchangeCmd;
import com.lppnb.dto.data.ExchangeRecordDTO;

public interface ExchangeService {

    SingleResponse<ExchangeRecordDTO> addExchangeRecord(ExchangeRecordAddCmd cmd);

    Response updateExchangeRecord(ExchangeRecordUpdateCmd cmd);

    Response exchangeItem(ItemExchangeCmd cmd);

}
