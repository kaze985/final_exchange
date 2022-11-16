package com.lppnb.domain.gateway;

import com.lppnb.domain.model.ExchangeRecord;

public interface ExchangeGateway {
    int add(ExchangeRecord record);
    int update(ExchangeRecord record);
}
