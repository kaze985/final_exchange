package com.geekgame.demo.dao;

import com.geekgame.demo.dataobject.ExchangeRecordDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExchangeRecordDAO {
    int add(ExchangeRecordDO recordDO);
    int update(ExchangeRecordDO recordDO);
}
