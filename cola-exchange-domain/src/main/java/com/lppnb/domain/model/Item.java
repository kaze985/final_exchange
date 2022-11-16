package com.lppnb.domain.model;

import com.alibaba.cola.domain.Entity;
import lombok.Data;

@Data
@Entity
public class Item {
    /**
     * 物品id
     */
    private String id;

    /**
     * 物品名
     */
    private String name;

    /**
     * 物品拥有者id
     */
    private String ownerId;

    /**
     * 物品拥有者用户名
     */
    private String ownerName;
}
