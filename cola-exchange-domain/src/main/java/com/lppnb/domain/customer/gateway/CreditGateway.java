package com.lppnb.domain.customer.gateway;

import com.lppnb.domain.customer.Credit;

//Assume that the credit info is in another distributed Service
public interface CreditGateway {
    Credit getCredit(String customerId);
}
