package com.yukari.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
public class UEnter implements Serializable {

    private Integer id;
    private Integer room_id;
    private Integer uid;
    private String uname;
    private String headIcon_url;
    private Integer level;
    private String date;

}
