package com.lppnb.gatewayimpl;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import com.lppnb.domain.gateway.ItemGateway;
import com.lppnb.domain.model.Item;
import com.lppnb.dto.data.ItemDTO;
import com.lppnb.gatewayimpl.rpc.ItemMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemGatewayImpl implements ItemGateway {

    @Autowired
    private ItemMapper itemMapper;

    @Override
    public Response updateItem(Item item) {
        return itemMapper.updateItem(item);
    }

    @Override
    public SingleResponse<Item> getItem(String id) {
        ItemDTO itemDTO = itemMapper.getItem(id).getData();
        Item item = new Item();
        BeanUtils.copyProperties(itemDTO, item);
        return SingleResponse.of(item);
    }
}
