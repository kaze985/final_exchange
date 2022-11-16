package com.lppnb.web;

import com.alibaba.cola.dto.Response;
import com.lppnb.api.ExchangeService;
import com.lppnb.dto.ExchangeRecordAddCmd;
import com.lppnb.dto.ExchangeRecordUpdateCmd;
import com.lppnb.dto.ItemExchangeCmd;
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
    public Response add(@RequestBody ExchangeRecordAddCmd cmd){
        return exchangeService.addExchangeRecord(cmd);
    }

    @PostMapping("/update")
    public Response update(@RequestBody ExchangeRecordUpdateCmd cmd){
        return exchangeService.updateExchangeRecord(cmd);
    }

    @PostMapping("/exchange")
    public Response exchange(@RequestBody ItemExchangeCmd cmd){
        return exchangeService.exchangeItem(cmd);
    }
    
}
