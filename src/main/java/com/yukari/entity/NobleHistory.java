package com.yukari.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class NobleHistory implements Serializable {

    private Integer id;
    private Integer type;
    private Integer uid;
    private String uname;
    private String headIcon_url;
    private Integer room_id;
    private String anchor_name;
    private Integer noble_type;
    private String noble_name;
    private String date;

}
