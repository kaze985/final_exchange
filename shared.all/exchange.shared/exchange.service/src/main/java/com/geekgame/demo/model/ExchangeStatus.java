package com.geekgame.demo.model;

import java.io.Serializable;

public enum ExchangeStatus implements Serializable {
    EXCHANGE_SUCCESS("交换成功"),
    EXCHANGE_FAILED("交换失败"),
    EXCHANGING("交换中");

    private final String statusName;

    ExchangeStatus(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }

}
