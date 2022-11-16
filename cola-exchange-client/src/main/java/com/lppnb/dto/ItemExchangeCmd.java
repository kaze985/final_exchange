package com.lppnb.dto;

import com.lppnb.dto.data.ExchangeRecordDTO;
import lombok.Data;
import lombok.NonNull;

@Data
public class ItemExchangeCmd {
    @NonNull
    private ExchangeRecordDTO exchangeRecordDTO;
}
