package com.yukari.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Anchor implements Serializable{

    private Integer id;
    private String anchor_name;
    private Integer room_id;
    private Integer uid;

    public Anchor(String anchor_name, Integer room_id, Integer uid) {
        this.anchor_name = anchor_name;
        this.room_id = room_id;
        this.uid = uid;
    }

}
