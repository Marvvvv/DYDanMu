package com.yukari.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class GiftRadio implements Serializable {

    private Integer id;
    private Integer room_id;
    private String giver;
    private String anchor_name;
    private Integer gift_id;
    private String gift_name;
    private Integer amount;
    private Integer gift_style;
    private String date;


}
