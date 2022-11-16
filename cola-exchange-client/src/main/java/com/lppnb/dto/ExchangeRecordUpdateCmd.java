package com.lppnb.dto;

import com.lppnb.dto.data.ExchangeRecordDTO;
import lombok.Data;
import lombok.NonNull;

@Data
public class ExchangeRecordUpdateCmd {
    @NonNull
    private ExchangeRecordDTO exchangeRecordDTO;
}
