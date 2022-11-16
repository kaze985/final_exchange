package com.lppnb.gatewayimpl.rpc;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import com.lppnb.api.ItemService;
import com.lppnb.domain.model.Item;
import com.lppnb.dto.ItemGetQry;
import com.lppnb.dto.ItemUpdateCmd;
import com.lppnb.dto.data.ItemDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {
    @DubboReference
    private ItemService itemService;

    public Response updateItem(Item item) {
        ItemDTO itemDTO = new ItemDTO();
        BeanUtils.copyProperties(item, itemDTO);
        return itemService.updateItem(new ItemUpdateCmd(itemDTO));
    }

    public SingleResponse<ItemDTO> getItem(String id) {
        return itemService.getItem(new ItemGetQry(id));
    }
}
