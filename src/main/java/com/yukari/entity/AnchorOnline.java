package com.yukari.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AnchorOnline implements Serializable {

    private Integer id;
    private Integer romm_id;
    private Integer online_status;
    private String date;

}
