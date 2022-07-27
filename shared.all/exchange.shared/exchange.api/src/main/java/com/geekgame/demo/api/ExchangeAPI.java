package com.geekgame.demo.api;

import com.geekgame.demo.model.ExchangeRecord;
import com.geekgame.demo.model.Result;
import com.geekgame.demo.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/exchange")
public class ExchangeAPI {
    
    @Autowired
    private ExchangeService exchangeService;
    
    @PostMapping("/add")
    public Result<ExchangeRecord> add(@RequestBody ExchangeRecord record){
        Result<ExchangeRecord> result = new Result<>();
        ExchangeRecord add = exchangeService.add(record);
        result.setSuccess(true);
        result.setData(add);
        return result;
    }

    @PostMapping("/update")
    public Result<ExchangeRecord> update(@RequestBody ExchangeRecord record){
        Result<ExchangeRecord> result = new Result<>();
        ExchangeRecord update = exchangeService.update(record);
        result.setSuccess(true);
        result.setData(update);
        return result;
    }

    @PostMapping("/exchange")
    public Result<ExchangeRecord> exchange(@RequestBody ExchangeRecord record){
        Result<ExchangeRecord> result = new Result<>();
        ExchangeRecord exchange = exchangeService.exchange(record);
        result.setSuccess(true);
        result.setData(exchange);
        return result;
    }
    
}
