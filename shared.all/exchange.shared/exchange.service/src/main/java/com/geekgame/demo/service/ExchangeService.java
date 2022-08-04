package com.geekgame.demo.service;

import com.geekgame.demo.model.ExchangeRecord;

public interface ExchangeService {

    ExchangeRecord add(ExchangeRecord record);

    ExchangeRecord update(ExchangeRecord record);


    boolean exchange(ExchangeRecord record);

}
