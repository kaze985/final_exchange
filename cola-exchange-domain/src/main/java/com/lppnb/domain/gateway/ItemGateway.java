package com.lppnb.domain.gateway;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import com.lppnb.domain.model.Item;

public interface ItemGateway {
    Response updateItem(Item item);

    SingleResponse<Item> getItem(String id);
}
