package com.lppnb.dto;

import com.lppnb.dto.data.ExchangeRecordDTO;
import lombok.Data;
import lombok.NonNull;

@Data
public class ExchangeRecordAddCmd {
    @NonNull
    private ExchangeRecordDTO exchangeRecordDTO;
}
