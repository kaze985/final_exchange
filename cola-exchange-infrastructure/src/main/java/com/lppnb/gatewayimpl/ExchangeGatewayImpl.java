package com.lppnb.gatewayimpl;

import com.lppnb.convertor.ExchangeRecordConvertor;
import com.lppnb.domain.gateway.ExchangeGateway;
import com.lppnb.domain.model.ExchangeRecord;
import com.lppnb.gatewayimpl.database.ExchangeRecordDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExchangeGatewayImpl implements ExchangeGateway {

    @Autowired
    private ExchangeRecordDAO exchangeRecordDAO;

    @Override
    public int add(ExchangeRecord record) {
        return exchangeRecordDAO.add(ExchangeRecordConvertor.toDataObject(record));
    }

    @Override
    public int update(ExchangeRecord record) {
        return exchangeRecordDAO.update(ExchangeRecordConvertor.toDataObject(record));
    }
}
