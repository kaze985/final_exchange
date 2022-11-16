package com.lppnb.domain.customer.gateway;

import com.lppnb.domain.customer.Customer;

public interface CustomerGateway {
    Customer getByById(String customerId);
}
