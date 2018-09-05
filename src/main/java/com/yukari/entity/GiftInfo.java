package com.yukari.entity;



import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class GiftInfo implements Serializable {

    private Integer id;
    private Integer gift_id;
    private String gift_name;
    private String gift_pic_url;
    private Double gift_cost;

}
