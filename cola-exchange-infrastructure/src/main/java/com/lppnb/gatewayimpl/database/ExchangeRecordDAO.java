package com.lppnb.gatewayimpl.database;

import com.lppnb.gatewayimpl.database.dataobject.ExchangeRecordDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExchangeRecordDAO {
    int add(ExchangeRecordDO recordDO);
    int update(ExchangeRecordDO recordDO);
}
